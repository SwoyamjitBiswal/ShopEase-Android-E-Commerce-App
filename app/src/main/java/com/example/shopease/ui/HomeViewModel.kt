package com.example.shopease.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.Product
import com.example.shopease.data.ProductUiModel
import com.example.shopease.data.ShoppingRepository
import com.example.shopease.data.WishlistItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(private val shoppingRepository: ShoppingRepository) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> = combine(
        shoppingRepository.allProducts,
        shoppingRepository.allWishlistItems
    ) { products, wishlistItems ->
        val wishlistedIds = wishlistItems.map { it.productId }.toSet()
        val productUiModels = products.map { product ->
            ProductUiModel(
                product = product,
                isWishlisted = wishlistedIds.contains(product.id)
            )
        }

        HomeUiState(
            categories = productUiModels.map { it.product.category }.distinct(),
            dealsOfTheDay = productUiModels.shuffled().take(5),
            newArrivals = productUiModels.takeLast(10).reversed(),
            allProducts = productUiModels
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )
}

data class HomeUiState(
    val categories: List<String> = emptyList(),
    val dealsOfTheDay: List<ProductUiModel> = emptyList(),
    val newArrivals: List<ProductUiModel> = emptyList(),
    val allProducts: List<ProductUiModel> = emptyList()
)
