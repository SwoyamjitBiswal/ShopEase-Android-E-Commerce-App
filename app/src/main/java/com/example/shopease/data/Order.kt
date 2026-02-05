package com.example.shopease.data

import com.google.firebase.database.PropertyName

data class Order(
    @get:PropertyName("orderId") @set:PropertyName("orderId") var orderId: String = "",
    @get:PropertyName("date") @set:PropertyName("date") var date: Long = 0L,
    @get:PropertyName("totalAmount") @set:PropertyName("totalAmount") var totalAmount: Double = 0.0,
    @get:PropertyName("status") @set:PropertyName("status") var status: String = ""
)
