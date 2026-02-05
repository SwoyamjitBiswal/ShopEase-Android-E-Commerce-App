package com.example.shopease.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.ShippingAddress
import com.example.shopease.data.ShoppingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShippingAddressViewModel(private val repository: ShoppingRepository) : ViewModel() {

    // DEBUGGING: Temporarily providing an empty list
    private val _shippingAddresses = MutableStateFlow<List<ShippingAddress>>(emptyList())
    val shippingAddresses: StateFlow<List<ShippingAddress>> = _shippingAddresses.asStateFlow()

    fun insertAddress(address: ShippingAddress) {
        viewModelScope.launch {
            // DEBUGGING: Temporarily disabled
            // repository.insertAddress(address)
        }
    }
}
