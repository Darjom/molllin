package com.mollin.app.presentation.screen.cart

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
fun CartScreen(
    viewModel: CartViewModel = viewModel()
) {
    val context = LocalContext.current

    // Cart items
    val cartItems by viewModel.cartItems.collectAsState()
    val total = cartItems.sumOf { it.total }

    // Obtener instancia de UserViewModel con Factory
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(
            UserRepository(AppDatabase.getInstance(context).userDao())
        )
    )

    // Obtener UID del usuario logueado desde FirebaseAuth
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    // Cargar datos del usuario cuando el UID esté disponible
    LaunchedEffect(uid) {
        uid?.let { userViewModel.loadUser(it) }
    }

    // Observar usuario desde Room
    val userEntity = userViewModel.user.collectAsState().value

    Scaffold(
        bottomBar = {
            if (cartItems.isNotEmpty() && userEntity != null) {
                Button(
                    onClick = {
                        // Convertimos UserEntity a User (dominio)
                        val user = User(
                            name = userEntity.name,
                            phone = userEntity.phone,
                            address = userEntity.address
                        )

                        viewModel.placeOrder(user) { message ->
                            val whatsappNumber = "+59163744948"
                            val encodedMessage = URLEncoder.encode(message, "UTF-8")
                            val uri = Uri.parse("https://wa.me/$whatsappNumber?text=$encodedMessage")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
                ) {
                    Text("COMPRAR", fontSize = 18.sp)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            Text(
                "Carrito de compras",
                modifier = Modifier.padding(16.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            if (cartItems.isEmpty()) {
                Text(
                    "No hay productos en el carrito",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 16.sp
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemCard(item = item, onRemove = {
                            viewModel.removeFromCart(item)
                        })
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Total: $total Bs",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 80.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(item: CartItem, onRemove: () -> Unit) {
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
                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Cantidad: ${item.quantity}", fontSize = 16.sp)
                Text("Precio: ${item.total} Bs", fontSize = 16.sp)
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
