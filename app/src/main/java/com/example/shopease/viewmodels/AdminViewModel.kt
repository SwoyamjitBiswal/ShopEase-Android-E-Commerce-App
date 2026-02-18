package com.example.shopease.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.Product
import com.example.shopease.data.ShoppingRepository
import kotlinx.coroutines.launch

class AdminViewModel(private val repository: ShoppingRepository) : ViewModel() {

    val allProducts = repository.getAllProducts()

    fun deleteProduct(product: Product) = viewModelScope.launch {
        repository.deleteProduct(product)
    }
}
