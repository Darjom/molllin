package com.mollin.app.repository.order

import com.mollin.app.domain.order.Order
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OrderRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val ordersCollection = firestore.collection("orders")

    suspend fun generateOrderId(): String {
        val snapshot = ordersCollection.get().await()
        val count = snapshot.size() + 1
        return "PED-${count.toString().padStart(4, '0')}"
    }

    suspend fun uploadOrder(order: Order): Boolean {
        return try {
            ordersCollection.document(order.orderId).set(order).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
