package com.example.shopease.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.Product
import com.example.shopease.data.ShoppingRepository
import com.example.shopease.data.WishlistItem
import com.example.shopease.data.WishlistItemWithProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WishlistViewModel(private val repository: ShoppingRepository) : ViewModel() {

    // DEBUGGING: Temporarily providing an empty list with explicit type
    private val _wishlistItems = MutableStateFlow<List<WishlistItemWithProduct>>(emptyList())
    val wishlistItems: StateFlow<List<WishlistItemWithProduct>> = _wishlistItems.asStateFlow()

    fun toggleWishlist(product: Product) {
        viewModelScope.launch {
            // DEBUGGING: Temporarily disabled
            /*
            val wishlistItem = wishlistItems.value.find { it.product.id == product.id }?.wishlistItem
            
            if (wishlistItem != null) {
                repository.deleteWishlistItem(wishlistItem)
            } else {
                repository.addWishlistItem(WishlistItem(productId = product.id))
            }
            */
        }
    }
}
