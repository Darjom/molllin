package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.presentation.screen.index.IndexScreen
import com.example.myapplication.ui.theme.MyApplicationTheme

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
    Surface(color = Color.Transparent) {
        NavHost(
            navController  = navController,
            startDestination = "index"
        ) {
            composable("index") {
                IndexScreen(navController)
            }
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
