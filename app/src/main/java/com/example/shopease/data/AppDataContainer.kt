package com.example.shopease.data

import android.content.Context

// The duplicate AppContainer interface has been removed from this file.

class AppDataContainer(private val context: Context) : AppContainer {

    override val shoppingRepository: ShoppingRepository by lazy {
        ShoppingRepository(
            AppDatabase.getDatabase(context).productDao()
            // DEBUGGING: Temporarily disabling all other DAOs
            /*
            , AppDatabase.getDatabase(context).cartDao(),
            AppDatabase.getDatabase(context).wishlistDao(),
            AppDatabase.getDatabase(context).orderDao(),
            AppDatabase.getDatabase(context).shippingAddressDao(),
            AppDatabase.getDatabase(context).reviewDao(),
            AppDatabase.getDatabase(context).paymentMethodDao(),
            AppDatabase.getDatabase(context).categoryDao()
            */
        )
    }
}
