package com.example.shopease.data

import androidx.room.Embedded
import androidx.room.Relation

// This class represents the relationship between a WishlistItem and a Product
data class WishlistItemWithProduct(
    @Embedded val wishlistItem: WishlistItem,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: Product
)
