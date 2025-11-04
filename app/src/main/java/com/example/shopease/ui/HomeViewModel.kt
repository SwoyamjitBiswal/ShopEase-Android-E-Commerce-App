package com.example.shopease.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.Product
import com.example.shopease.data.ShoppingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(private val shoppingRepository: ShoppingRepository) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> = shoppingRepository.allProducts
        .map { products ->
            HomeUiState(
                categories = products.map { it.category }.distinct(),
                dealsOfTheDay = products.shuffled().take(5),
                newArrivals = products.takeLast(10).reversed(), // Assuming newer products have higher IDs
                allProducts = products
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )
}

data class HomeUiState(
    val categories: List<String> = emptyList(),
    val dealsOfTheDay: List<Product> = emptyList(),
    val newArrivals: List<Product> = emptyList(),
    val allProducts: List<Product> = emptyList()
)
