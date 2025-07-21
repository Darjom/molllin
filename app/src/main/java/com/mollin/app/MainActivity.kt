package com.mollin.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mollin.app.presentation.screen.cart.CartScreen
import com.mollin.app.presentation.screen.cart.CartViewModel
import com.mollin.app.presentation.screen.index.IndexScreen
import com.mollin.app.presentation.screen.login.LoginScreen
import com.mollin.app.presentation.screen.register.RegisterScreen
import com.mollin.app.presentation.screen.home.HomeScreen
import com.mollin.app.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AppNav()
            }
        }
    }
}

@Composable
fun AppNav() {
    val navController = rememberNavController()
    // Instancia compartida de CartViewModel
    val cartViewModel: CartViewModel = hiltViewModel()


    NavHost(
        navController = navController,
        startDestination = "index"
    ) {
        composable("index") {
            IndexScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("home") {
            HomeScreen(
                navController = navController,
                viewModel = hiltViewModel(), // PASA el viewModel directamente
                cartViewModel = cartViewModel
            )
        }
        composable("cart") {
            // Reutilizamos el mismo CartViewModel para CartScreen
            CartScreen(viewModel = cartViewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppNavPreview() {
    MyApplicationTheme {
        AppNav()
    }
}
