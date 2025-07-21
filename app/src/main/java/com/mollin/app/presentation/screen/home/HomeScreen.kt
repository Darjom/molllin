package com.mollin.app.presentation.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mollin.app.domain.order.CartItem
import com.mollin.app.presentation.screen.cart.CartViewModel
import com.mollin.app.ui.theme.GrisClaroNeutro
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val modalState = rememberModalBottomSheetState()
    val selectedProduct by viewModel.selectedProduct
    val productQuantity by viewModel.selectedQuantity
    val cartItems by cartViewModel.cartItems.collectAsState()
    val cartCount = cartItems.size

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisClaroNeutro)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BadgedBox(
                    badge = {
                        if (cartCount > 0) {
                            Badge { Text(cartCount.toString(), color = Color.White) }
                        }
                    }
                ) {
                    IconButton(onClick = { navController.navigate("cart") }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                    }
                }
            }

            Text(
                "Nuestros Productos",
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(viewModel.products) { product ->
                    ProductCard(product = product) {
                        val index = viewModel.products.indexOfFirst { it.id == product.id }
                        if (index != -1) {
                            viewModel.selectProduct(index)
                            scope.launch { modalState.show() }
                        }
                    }

                }
            }
        }

        selectedProduct?.let { prod ->
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch { modalState.hide() }
                    viewModel.clearSelected()
                },
                sheetState = modalState,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                containerColor = Color.Black
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = prod.imageRes),
                        contentDescription = prod.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(prod.name, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text(prod.description, color = Color.White, fontSize = 16.sp)
                    Spacer(Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { viewModel.decreaseQuantity() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Menos", tint = Color.White)
                        }
                        Text(productQuantity.toString(), color = Color.White, fontSize = 18.sp)
                        IconButton(onClick = { viewModel.increaseQuantity() }) {
                            Icon(Icons.Default.Add, contentDescription = "Más", tint = Color.White)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Total: ${prod.price * productQuantity} Bs",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            cartViewModel.addToCart(
                                CartItem(
                                    productId = prod.id,
                                    name = prod.name,
                                    quantity = productQuantity,
                                    price = prod.price,
                                    imageRes = prod.imageRes
                                )
                            )
                            viewModel.clearSelected()
                            scope.launch { modalState.hide() }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("AGREGAR A CARRITO")
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(product.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("${product.price} Bs", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}
