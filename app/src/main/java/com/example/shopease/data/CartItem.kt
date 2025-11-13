package com.example.shopease.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey val id: String, // Typically the same as the product ID
    val name: String,
    val price: Double,
    val imageUrl: String,
    var quantity: Int,
    val productId: String // Keep a reference to the original product
)
