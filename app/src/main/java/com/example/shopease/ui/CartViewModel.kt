package com.example.shopease.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.CartItem
import com.example.shopease.data.Order
import com.example.shopease.data.ShoppingRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

sealed class CartUiEvent {
    data class RemoveItem(val item: CartItem) : CartUiEvent()
    data class UpdateQuantity(val item: CartItem, val quantity: Int) : CartUiEvent()
    object PlaceOrder : CartUiEvent()
}

class CartViewModel(private val repository: ShoppingRepository) : ViewModel() {

    val cartItems: StateFlow<List<CartItem>> = repository.allCartItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: CartUiEvent) {
        when (event) {
            is CartUiEvent.RemoveItem -> viewModelScope.launch {
                repository.deleteCartItem(event.item)
            }
            is CartUiEvent.UpdateQuantity -> viewModelScope.launch {
                repository.updateCartItem(event.item.copy(quantity = event.quantity))
            }
            is CartUiEvent.PlaceOrder -> viewModelScope.launch {
                val order = Order(
                    orderId = System.currentTimeMillis().toString(),
                    items = cartItems.value,
                    total = cartItems.value.sumOf { it.price * it.quantity },
                    orderDate = Date()
                )
                repository.insertOrder(order)
                repository.clearCart()
                _uiEvent.send(UiEvent.NavigateToOrderSuccess)
            }
        }
    }

    sealed class UiEvent {
        object NavigateToOrderSuccess : UiEvent()
    }
}
