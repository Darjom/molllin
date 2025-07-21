package com.mollin.app.presentation.screen.index

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
import com.mollin.app.ui.theme.VerdeMolleOscuro
import com.mollin.app.ui.theme.VerdeClaro
import com.mollin.app.R

@Composable
fun IndexScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VerdeClaro)
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
                modifier = Modifier.size(300.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Botón INICIAR SESIÓN
            Button(
                onClick = { navController.navigate("login") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height( 56.dp ) // altura estándar Material
            ) {
                Text(text = "INICIAR SESIÓN", color = VerdeMolleOscuro)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column( /* … */ ) {
                /* Logo y primer botón */

                // Botón REGÍSTRATE con fondo oscuro
                Button(
                    onClick = { navController.navigate("register") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeMolleOscuro
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(text = "REGÍSTRATE", color = Color.White )
                }
            }
        }
    }
}
