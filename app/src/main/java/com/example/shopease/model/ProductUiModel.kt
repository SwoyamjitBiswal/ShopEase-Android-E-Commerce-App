package com.example.shopease.model

import com.example.shopease.data.Product

// A UI model that combines product data with UI-specific state (e.g., isWished)
data class ProductUiModel(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val description: String,
    val rating: Float, // Added missing property
    val isWished: Boolean,
    val originalProduct: Product // Keep the original product for business logic
)
