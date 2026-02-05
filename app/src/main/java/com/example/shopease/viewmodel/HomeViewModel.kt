package com.example.shopease.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shopease.data.Product
import com.example.shopease.data.ProductRepository
import kotlinx.coroutines.flow.Flow

class HomeViewModel(private val repository: ProductRepository) : ViewModel() {

    val products: Flow<List<Product>> = repository.getAllProducts()
}
