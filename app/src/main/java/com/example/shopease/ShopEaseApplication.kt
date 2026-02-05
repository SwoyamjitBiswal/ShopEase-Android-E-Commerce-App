package com.example.shopease

import android.app.Application
import com.example.shopease.data.AppDatabase

class ShopEaseApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}
