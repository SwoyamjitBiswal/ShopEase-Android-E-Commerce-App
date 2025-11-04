package com.example.shopease.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.Product
import com.example.shopease.data.ShoppingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(private val repository: ShoppingRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.RELEVANCE)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    val searchResults: StateFlow<List<Product>> = combine(
        repository.allProducts,
        searchQuery,
        sortOrder
    ) { products, query, sortOrder ->
        val filteredList = if (query.isBlank()) {
            products
        } else {
            val categories = products.map { it.category }.distinct()
            if (categories.any { it.equals(query, ignoreCase = true) }) {
                products.filter { it.category.equals(query, ignoreCase = true) }
            } else {
                products.filter { it.name.contains(query, ignoreCase = true) }
            }
        }

        when (sortOrder) {
            SortOrder.RELEVANCE -> filteredList // Default, no change
            SortOrder.PRICE_LOW_TO_HIGH -> filteredList.sortedBy { it.price }
            SortOrder.PRICE_HIGH_TO_LOW -> filteredList.sortedByDescending { it.price }
            SortOrder.RATING -> filteredList.sortedByDescending { it.rating }
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

enum class SortOrder {
    RELEVANCE,
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW,
    RATING
}
