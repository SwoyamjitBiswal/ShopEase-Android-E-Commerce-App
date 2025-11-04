package com.example.shopease.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.Product
import com.example.shopease.data.ShoppingRepository
import com.example.shopease.data.WishlistItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class WishlistUiState(
    val wishlistItems: List<Product> = emptyList()
)

class WishlistViewModel(private val repository: ShoppingRepository) : ViewModel() {

    val wishlistUiState: StateFlow<WishlistUiState> = combine(
        repository.allProducts,
        repository.allWishlistItems
    ) { products, wishlistItems ->
        val wishlistedProducts = wishlistItems.mapNotNull { wishlistItem ->
            products.find { it.id == wishlistItem.productId }
        }
        WishlistUiState(wishlistedProducts)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = WishlistUiState()
    )

    fun addProductToWishlist(product: Product) {
        viewModelScope.launch {
            repository.addWishlistItem(WishlistItem(product.id))
        }
    }

    fun removeProductFromWishlist(product: Product) {
        viewModelScope.launch {
            repository.deleteWishlistItem(WishlistItem(product.id))
        }
    }

    fun isWishlisted(product: Product, uiState: WishlistUiState): Boolean {
        return uiState.wishlistItems.any { it.id == product.id }
    }
}
