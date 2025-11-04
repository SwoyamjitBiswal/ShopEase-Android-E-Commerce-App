package com.example.shopease.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.Product
import com.example.shopease.data.ShoppingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminViewModel(private val shoppingRepository: ShoppingRepository) : ViewModel() {

    val adminUiState: StateFlow<AdminUiState> = shoppingRepository.allProducts
        .map { AdminUiState(productList = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AdminUiState()
        )

    fun addProduct(product: Product) {
        viewModelScope.launch {
            // Generate a unique ID for the new product
            val newProduct = product.copy(id = System.currentTimeMillis())
            shoppingRepository.insertProduct(newProduct)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            shoppingRepository.updateProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            shoppingRepository.deleteProduct(product)
        }
    }
}

data class AdminUiState(
    val productList: List<Product> = emptyList()
)
