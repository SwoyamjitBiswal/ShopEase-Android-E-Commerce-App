package com.example.shopease.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val fullName: String,
    val addressLine1: String,
    val city: String,
    val postalCode: String,
    val country: String,
    val isDefault: Boolean = false
) : Parcelable
