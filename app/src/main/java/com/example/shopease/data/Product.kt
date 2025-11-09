package com.example.shopease.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "products")
@TypeConverters(ReviewListConverter::class) // Use the new converter
data class Product(
    @PrimaryKey
    val id: Long = 0,
    val name: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val category: String = "",
    val rating: Float = 0f,
    val reviews: List<Review> = emptyList()
) : Parcelable
