package com.example.shopease.data

import android.content.Context

class AppDataContainer(private val context: Context) {
    private val database by lazy { AppDatabase.getDatabase(context) }

    val shoppingRepository: ShoppingRepository by lazy {
        ShoppingRepository(database.productDao(), database.cartDao(), database.wishlistDao())
    }
}
