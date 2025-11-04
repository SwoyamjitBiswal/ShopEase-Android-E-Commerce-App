package com.example.shopease.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.ShippingAddress
import com.example.shopease.data.ShoppingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShippingAddressViewModel(private val repository: ShoppingRepository) : ViewModel() {

    val shippingAddresses: StateFlow<List<ShippingAddress>> = repository.getAddressesForUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun insertAddress(address: ShippingAddress) {
        viewModelScope.launch {
            repository.insertAddress(address)
        }
    }
}
