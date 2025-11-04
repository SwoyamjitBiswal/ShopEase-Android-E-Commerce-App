package com.example.shopease.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Review(
    val userId: String = "",
    val userName: String = "",
    val userImageUrl: String? = null,
    val rating: Float = 0f,
    val comment: String = "",
    val date: Date = Date()
) : Parcelable
