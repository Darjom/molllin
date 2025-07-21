package com.mollin.app.presentation.screen.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mollin.app.R

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val imageRes: Int
)

class HomeViewModel : ViewModel() {
    private val _products = listOf(
        Product("1","Mollin 500 mL", "Limpieza natural en cada gota\nLimpia, desinfecta y protege con el poder natural del molle. Ideal para uso diario en espacios pequeños, esta presentación es práctica, ecológica y perfecta para hogares que buscan frescura y bienestar sin químicos agresivos.", 15, R.drawable.product1),
        Product("2","Mollin 900 mL", "Limpieza prolongada y ecológica\nEl desinfectante natural que combina limpieza profunda con acción repelente contra zancudos y moscas. Mollin 900 mL rinde más, cuida tu hogar, tu salud y el medio ambiente, con el aroma fresco y auténtico del molle andino.", 23, R.drawable.product2)
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
