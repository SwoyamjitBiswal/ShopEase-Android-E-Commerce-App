package com.example.shopease.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey val id: String,
    val productId: String,
    val userId: String,
    val userName: String,
    val userImageUrl: String, // Added missing property
    val rating: Float,
    val comment: String,
    val date: Long
)
