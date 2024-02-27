package com.venkatesh.sneakership.data.model

data class CartState(
    val orderSneakers: List<OrderItem>,
    val totalPrice: Int
)
