package com.example.shopease

import android.app.Application
import com.example.shopease.data.AppContainer
import com.example.shopease.data.AppDataContainer

class ShopEaseApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
