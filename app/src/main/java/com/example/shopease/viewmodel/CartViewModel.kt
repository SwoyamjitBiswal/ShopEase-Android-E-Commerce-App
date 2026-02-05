package com.example.shopease.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.CartItem
import com.example.shopease.data.CartItemWithProduct
import com.example.shopease.data.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CartViewModel(private val repository: CartRepository) : ViewModel() {

    val allCartItems: Flow<List<CartItemWithProduct>> = repository.getAllCartItems()

    fun insert(item: CartItem) = viewModelScope.launch {
        repository.insert(item)
    }

    fun update(item: CartItem) = viewModelScope.launch {
        repository.update(item)
    }

    fun delete(item: CartItem) = viewModelScope.launch {
        repository.delete(item)
    }

    fun clearCart() = viewModelScope.launch {
        repository.clearCart()
    }
}
