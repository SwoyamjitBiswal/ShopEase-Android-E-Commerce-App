package com.example.shopease.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey val id: String = "",
    val productId: String = "", // Foreign key to Product
    val userId: String = "",
    val userName: String = "",
    val userImageUrl: String? = null,
    val rating: Float = 0f,
    val comment: String = "",
    val date: Date = Date()
) : Parcelable
