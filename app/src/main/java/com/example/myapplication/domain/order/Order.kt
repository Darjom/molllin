package com.example.myapplication.domain.order

import com.example.myapplication.domain.user.User

data class Order(
    val orderId: String,
    val user: User,
    val items: List<CartItem>,
    val totalAmount: Int
)
