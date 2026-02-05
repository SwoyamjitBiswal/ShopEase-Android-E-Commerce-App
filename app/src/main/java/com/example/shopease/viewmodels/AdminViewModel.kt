package com.example.shopease.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.Product
import com.example.shopease.data.Review
import com.example.shopease.data.ShoppingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminUiState(
    val productList: List<Product> = emptyList(),
    val reviewList: List<Review> = emptyList()
)

class AdminViewModel(private val repository: ShoppingRepository) : ViewModel() {

    private val _adminUiState = MutableStateFlow(AdminUiState())
    val adminUiState: StateFlow<AdminUiState> = _adminUiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allProducts.collect { products ->
                _adminUiState.update { it.copy(productList = products) }
            }
        }
        /* DEBUGGING: Temporarily disabled
        viewModelScope.launch {
            repository.allReviews.collect { reviews ->
                _adminUiState.update { it.copy(reviewList = reviews) }
            }
        }
        */
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            // DEBUGGING: Temporarily disabled
            // repository.insertProduct(product)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            // DEBUGGING: Temporarily disabled
            // repository.updateProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            // DEBUGGING: Temporarily disabled
            // repository.deleteProduct(product)
        }
    }

    fun deleteReview(review: Review) {
        // DEBUGGING: Temporarily disabled
    }
}
