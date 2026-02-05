package com.example.shopease.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shipping_addresses")
data class ShippingAddress(
    @PrimaryKey val id: String,
    val userId: String,
    val fullName: String,
    val addressLine1: String,
    val addressLine2: String?,
    val city: String,
    val state: String,
    val zipCode: String,
    val phoneNumber: String,
    var isDefault: Boolean = false
)
