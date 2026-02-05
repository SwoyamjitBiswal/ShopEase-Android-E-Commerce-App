package com.example.shopease.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.ShoppingRepository
import com.example.shopease.model.ProductUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

enum class SortOrder { RELEVANCE, PRICE_LOW_TO_HIGH, PRICE_HIGH_TO_LOW, RATING }

class SearchViewModel(private val repository: ShoppingRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.RELEVANCE)
    val sortOrder = _sortOrder.asStateFlow()

    // DEBUGGING: Temporarily providing an empty list with explicit type
    private val _products = MutableStateFlow<List<ProductUiModel>>(emptyList())
    private val products: StateFlow<List<ProductUiModel>> = _products.asStateFlow()

    // DEBUGGING: Temporarily providing an empty list with explicit type
    private val _searchResults = MutableStateFlow<List<ProductUiModel>>(emptyList())
    val searchResults: StateFlow<List<ProductUiModel>> = _searchResults.asStateFlow()

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onSortOrderChanged(order: SortOrder) {
        _sortOrder.value = order
    }
}
