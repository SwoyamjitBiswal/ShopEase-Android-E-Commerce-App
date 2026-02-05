package com.example.shopease.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_methods")
data class PaymentMethod(
    @PrimaryKey val id: String,
    val userId: String,
    val cardType: String,
    val cardNumber: String,
    val cardHolderName: String,
    val expiryDate: String
)
