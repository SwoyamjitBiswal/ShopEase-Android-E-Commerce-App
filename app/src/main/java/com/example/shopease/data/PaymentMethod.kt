package com.example.shopease.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentMethod(
    val cardType: String, // e.g., "Visa", "Mastercard"
    val lastFourDigits: String,
    val expiryDate: String
) : Parcelable
