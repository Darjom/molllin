package com.mollin.app.domain.order

data class CartItem(
    val productId: String,
    val name: String,
    val quantity: Int,
    val price: Int,
    val imageRes: Int
) {
    val total: Int
        get() = quantity * price
}
