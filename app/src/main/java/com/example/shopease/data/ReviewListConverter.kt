package com.example.shopease.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ReviewListConverter {
    @TypeConverter
    fun fromReviewList(value: String?): List<Review>? {
        val listType = object : TypeToken<List<Review>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toReviewList(list: List<Review>?): String? {
        return Gson().toJson(list)
    }
}
