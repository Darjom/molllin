package com.example.myapplication.presentation.screen.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myapplication.R

data class Product(
    val name: String,
    val description: String,
    val price: Int,
    val imageRes: Int
)

class HomeViewModel : ViewModel() {
    private val _products = listOf(
        Product("Mollin 500 mL", "Desinfectante multiusos 500ml.", 15, R.drawable.product1),
        Product("Mollin 900 mL", "Desinfectante multiusos 900ml.", 23, R.drawable.product2)
    )

    val products: List<Product> = _products

    var selectedProduct = mutableStateOf<Product?>(null)
    var selectedQuantity = mutableStateOf(1)
    var cartCount = mutableStateOf(0)

    fun selectProduct(index: Int) {
        selectedProduct.value = products.getOrNull(index)
        selectedQuantity.value = 1
    }

    fun clearSelected() {
        selectedProduct.value = null
        selectedQuantity.value = 1
    }

    fun increaseQuantity() {
        selectedQuantity.value++
    }

    fun decreaseQuantity() {
        if (selectedQuantity.value > 1) selectedQuantity.value--
    }

    fun addToCart() {
        cartCount.value += selectedQuantity.value
        clearSelected()
    }
}
