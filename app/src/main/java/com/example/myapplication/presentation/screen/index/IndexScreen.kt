package com.example.myapplication.presentation.screen.index

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun IndexScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFA8D5BA)) // Mint green de fondo
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo arriba
            Image(
                painter = painterResource(id = R.drawable.logo_negativo_sin_fondo),
                contentDescription = "Logo Mollín",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Botón INICIAR SESIÓN
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height( 56.dp ) // altura estándar Material
            ) {
                Text(text = "INICIAR SESIÓN", color = Color(0xFF194D33))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column( /* … */ ) {
                /* Logo y primer botón */

                // Botón REGÍSTRATE con fondo oscuro
                Button(
                    onClick = { navController.navigate("register") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF194D33)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(text = "REGÍSTRATE")
                }
            }
        }
    }
}
