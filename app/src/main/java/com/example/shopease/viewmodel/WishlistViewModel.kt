package com.example.shopease.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.Product
import com.example.shopease.data.WishlistItem
import com.example.shopease.data.WishlistItemWithProduct
import com.example.shopease.data.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WishlistViewModel(private val repository: WishlistRepository) : ViewModel() {

    val allWishlistItems: Flow<List<WishlistItemWithProduct>> = repository.getAllWishlistItems()

    fun toggleWishlist(product: Product) = viewModelScope.launch {
        val wishlist = allWishlistItems.first()
        val wishlistItem = wishlist.find { it.product.id == product.id }?.wishlistItem
        if (wishlistItem == null) {
            repository.insert(WishlistItem(productId = product.id))
        } else {
            repository.delete(wishlistItem)
        }
    }
}
