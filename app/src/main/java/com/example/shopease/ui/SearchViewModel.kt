package com.example.shopease.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.ProductUiModel
import com.example.shopease.data.ShoppingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class SortOrder { RELEVANCE, PRICE_LOW_TO_HIGH, PRICE_HIGH_TO_LOW, RATING }

class SearchViewModel(private val repository: ShoppingRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.RELEVANCE)
    val sortOrder = _sortOrder.asStateFlow()

    val searchResults: StateFlow<List<ProductUiModel>> = combine(
        repository.allProducts,
        repository.allWishlistItems,
        _searchQuery,
        _sortOrder
    ) { products, wishlistItems, query, sortOrder ->
        val wishlistedIds = wishlistItems.map { it.productId }.toSet()

        val filteredProducts = if (query.isBlank()) {
            emptyList()
        } else {
            products.filter { it.name.contains(query, ignoreCase = true) || it.category.contains(query, ignoreCase = true) }
        }

        val sortedProducts = when (sortOrder) {
            SortOrder.RELEVANCE -> filteredProducts // Simple relevance, can be improved
            SortOrder.PRICE_LOW_TO_HIGH -> filteredProducts.sortedBy { it.price }
            SortOrder.PRICE_HIGH_TO_LOW -> filteredProducts.sortedByDescending { it.price }
            SortOrder.RATING -> filteredProducts.sortedByDescending { it.rating } // Assuming Product has a rating
        }

        sortedProducts.map { product ->
            ProductUiModel(
                product = product,
                isWishlisted = wishlistedIds.contains(product.id)
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onSortOrderChanged(sortOrder: SortOrder) {
        _sortOrder.value = sortOrder
    }
}
