package com.example.shopease.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromCartItemList(items: List<CartItem>?): String? {
        return items?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toCartItemList(json: String?): List<CartItem>? {
        if (json == null) {
            return emptyList()
        }
        return try {
            val type = object : TypeToken<List<CartItem>>() {}.type
            gson.fromJson(json, type)
        } catch (e: JsonSyntaxException) {
            // If parsing fails (due to old, bad data), return an empty list instead of crashing.
            emptyList()
        }
    }
}
