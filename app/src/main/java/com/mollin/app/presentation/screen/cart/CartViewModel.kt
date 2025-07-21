package com.mollin.app.presentation.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mollin.app.domain.order.CartItem
import com.mollin.app.domain.order.Order
import com.mollin.app.domain.user.User
import com.mollin.app.repository.order.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val orderRepository: OrderRepository = OrderRepository()
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _orderSuccess = MutableStateFlow<Boolean?>(null)
    val orderSuccess: StateFlow<Boolean?> = _orderSuccess

    fun addToCart(item: CartItem) {
        _cartItems.value = _cartItems.value + item
    }

    fun removeFromCart(item: CartItem) {
        _cartItems.value = _cartItems.value - item
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun placeOrder(user: User, onWhatsappMessageReady: (String) -> Unit) {
        viewModelScope.launch {
            val orderId = orderRepository.generateOrderId()
            val totalAmount = _cartItems.value.sumOf { it.total }
            val order = Order(
                orderId = orderId,
                user = user,
                items = _cartItems.value,
                totalAmount = totalAmount
            )

            val success = orderRepository.uploadOrder(order)
            _orderSuccess.value = success

            if (success) {
                val message = buildWhatsappMessage(order)
                onWhatsappMessageReady(message)
                clearCart()
            }
        }
    }

    private fun buildWhatsappMessage(order: Order): String {
        val builder = StringBuilder()
        builder.append("Número de pedido: ${order.orderId}\n")
        builder.append("Nombre del cliente: ${order.user.name}\n")
        builder.append("Número de celular: ${order.user.phone}\n")
        builder.append("Dirección: ${order.user.address}\n")
        builder.append("\nOrden:\n")

        order.items.forEach {
            builder.append("- ${it.name} x${it.quantity} → ${it.total} Bs\n")
        }

        builder.append("\nTotal a pagar: ${order.totalAmount} Bs")
        return builder.toString()
    }
}