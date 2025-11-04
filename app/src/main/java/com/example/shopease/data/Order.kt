package com.example.shopease.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

@Entity(tableName = "orders")
@TypeConverters(CartItemListConverter::class)
data class Order(
    @PrimaryKey
    val orderId: String,
    val items: List<CartItem>,
    val total: Double,
    val date: Date
)

class CartItemListConverter {
    @TypeConverter
    fun fromString(value: String): List<CartItem> {
        val listType = object : TypeToken<List<CartItem>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<CartItem>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
