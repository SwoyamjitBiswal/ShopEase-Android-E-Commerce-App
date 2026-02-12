package com.example.shopease.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shopease.data.HomeItem
import com.example.shopease.data.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HomeViewModel(private val repository: ProductRepository) : ViewModel() {

    val homeItems: Flow<List<HomeItem>> = repository.getAllProducts().map {
        val items = mutableListOf<HomeItem>()
        items.add(HomeItem.HeaderItem("Featured Products"))
        items.addAll(it.map { product -> HomeItem.ProductItem(product) })
        items
    }
}
