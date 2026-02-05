package com.example.shopease.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.Category
import com.example.shopease.data.HomeItem
import com.example.shopease.data.ShoppingRepository
import com.example.shopease.model.ProductUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(private val repository: ShoppingRepository) : ViewModel() {

    // DEBUGGING: Temporarily providing an empty list with explicit type
    private val _products = MutableStateFlow<List<ProductUiModel>>(emptyList())
    private val products: StateFlow<List<ProductUiModel>> = _products.asStateFlow()

    // DEBUGGING: Temporarily providing an empty list with explicit type
    private val _homeItems = MutableStateFlow<List<HomeItem>>(emptyList())
    val homeItems: StateFlow<List<HomeItem>> = _homeItems.asStateFlow()

}
