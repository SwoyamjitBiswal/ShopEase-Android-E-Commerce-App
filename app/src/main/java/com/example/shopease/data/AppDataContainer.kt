package com.example.shopease.data

import android.content.Context

class AppDataContainer(private val context: Context) : AppContainer {

    override val shoppingRepository: ShoppingRepository by lazy {
        val database = AppDatabase.getDatabase(context)
        ShoppingRepository(
            database.productDao(),
            database.cartDao(),
            database.wishlistDao(),
            database.orderDao(),
            database.shippingAddressDao(),
            database.reviewDao(),
            database.paymentMethodDao()
        )
    }
}
