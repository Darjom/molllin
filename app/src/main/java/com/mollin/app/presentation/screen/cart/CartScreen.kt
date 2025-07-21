package com.mollin.app.presentation.screen.cart

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mollin.app.data.AppDatabase
import com.mollin.app.data.user.UserRepository
import com.mollin.app.domain.order.CartItem
import com.mollin.app.domain.user.User
import com.mollin.app.presentation.user.UserViewModel
import com.mollin.app.presentation.user.UserViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(viewModel: CartViewModel = viewModel()) {
    val context = LocalContext.current
    val cartItems by viewModel.cartItems.collectAsState()
    val total = cartItems.sumOf { it.total }

    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black

    // Cargar usuario desde Room
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(
            UserRepository(AppDatabase.getInstance(context).userDao())
        )
    )
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    LaunchedEffect(uid) { uid?.let { userViewModel.loadUser(it) } }
    val userEntity = userViewModel.user.collectAsState().value

    Scaffold(
        bottomBar = {
            if (cartItems.isNotEmpty() && userEntity != null) {
                Column(Modifier.navigationBarsPadding()) {
                    Button(
                        onClick = {
                            val user = User(
                                name = userEntity.name,
                                phone = userEntity.phone,
                                address = userEntity.address
                            )
                            viewModel.placeOrder(user) { message ->
                                val encoded = URLEncoder.encode(message, "UTF-8")
                                val uri = Uri.parse("https://wa.me/+59175483831?text=$encoded")
                                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
                    ) {
                        Text("COMPRAR", fontSize = 18.sp, color = Color.White)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(backgroundColor)
        ) {
            Text(
                "Carrito de compras",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.padding(16.dp)
            )

            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay productos en el carrito", fontSize = 16.sp, color = textColor)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(cartItems) { item ->
                        CartItemCard(item = item, textColor = textColor) {
                            viewModel.removeFromCart(item)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Total: $total Bs",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun CartItemCard(item: CartItem, textColor: Color, onRemove: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor)
                Text("Cantidad: ${item.quantity}", fontSize = 16.sp, color = textColor)
                Text("Precio: ${item.total} Bs", fontSize = 16.sp, color = textColor)
            }

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar",
                tint = Color.Red,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onRemove() }
            )
        }
    }
}
