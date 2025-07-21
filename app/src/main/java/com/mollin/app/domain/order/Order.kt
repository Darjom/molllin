package com.mollin.app.domain.order

import com.mollin.app.domain.user.User

data class Order(
    val orderId: String,
    val user: User,
    val items: List<CartItem>,
    val totalAmount: Int
)
