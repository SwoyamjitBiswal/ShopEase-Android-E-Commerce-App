package com.example.shopease.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey
    val productId: Long = 0,
    var quantity: Int = 0
) : Parcelable
