package com.mollin.app.presentation.screen.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mollin.app.ui.theme.Blanco
import com.mollin.app.ui.theme.VerdeMolleOscuro

@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.Center) {
            Text("Regístrate", modifier = Modifier.padding(bottom = 16.dp), fontSize = 24.sp, fontWeight = FontWeight.Bold)

            // Nombre
            OutlinedTextField(
                value = state.fullName, onValueChange = viewModel::onNameChange,
                label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            // Correo
            OutlinedTextField(
                value = state.email, onValueChange = viewModel::onEmailChange,
                label = { Text("Correo") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            // Contraseña
            OutlinedTextField(
                value = state.password, onValueChange = viewModel::onPasswordChange,
                label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            // Dirección
            OutlinedTextField(
                value = state.address, onValueChange = viewModel::onAddressChange,
                label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            // Teléfono
            OutlinedTextField(
                value = state.phone, onValueChange = viewModel::onPhoneChange,
                label = { Text("Teléfono") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            Button(onClick = { viewModel.onRegisterClick { navController.popBackStack() } },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeMolleOscuro, contentColor = Blanco)
            ) {
                Text("REGISTRARSE")
            }

            Spacer(Modifier.height(12.dp))
            if (state.isLoading) CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            state.error?.let { Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp)) }
        }
    }
}
