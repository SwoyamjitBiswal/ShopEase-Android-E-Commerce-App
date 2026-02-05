package com.example.shopease.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.CartItem
import com.example.shopease.data.CartItemWithProduct
import com.example.shopease.data.Order
import com.example.shopease.data.Product
import com.example.shopease.data.ShoppingRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed class CartUiEvent {
    data class AddItem(val product: Product) : CartUiEvent()
    data class RemoveItem(val item: CartItemWithProduct) : CartUiEvent()
    data class UpdateQuantity(val item: CartItemWithProduct, val quantity: Int) : CartUiEvent()
    object PlaceOrder : CartUiEvent()
}

class CartViewModel(private val repository: ShoppingRepository) : ViewModel() {

    // DEBUGGING: Temporarily providing an empty list with explicit type
    private val _cartItems = MutableStateFlow<List<CartItemWithProduct>>(emptyList())
    val cartItems: StateFlow<List<CartItemWithProduct>> = _cartItems.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: CartUiEvent) {
        when (event) {
            is CartUiEvent.AddItem -> viewModelScope.launch {
                /* DEBUGGING: Temporarily disabled
                val existingCartItem = cartItems.value.find { it.product.id == event.product.id }?.cartItem
                if (existingCartItem != null) {
                    repository.updateCartItem(existingCartItem.copy(quantity = existingCartItem.quantity + 1))
                } else {
                    val newCartItem = CartItem(productId = event.product.id, quantity = 1)
                    repository.addCartItem(newCartItem)
                }
                */
            }
            is CartUiEvent.RemoveItem -> viewModelScope.launch {
                // DEBUGGING: Temporarily disabled
                // repository.deleteCartItem(event.item.cartItem)
            }
            is CartUiEvent.UpdateQuantity -> viewModelScope.launch {
                // DEBUGGING: Temporarily disabled
                // repository.updateCartItem(event.item.cartItem.copy(quantity = event.quantity))
            }
            is CartUiEvent.PlaceOrder -> viewModelScope.launch {
                /* DEBUGGING: Temporarily disabled
                val orderItems = cartItems.value.map { it.cartItem }
                val order = Order(
                    orderId = System.currentTimeMillis().toString(),
                    items = orderItems,
                    total = cartItems.value.sumOf { it.product.price * it.cartItem.quantity },
                    orderDate = System.currentTimeMillis()
                )
                repository.insertOrder(order)
                repository.clearCart()
                _uiEvent.send(UiEvent.NavigateToOrderSuccess)
                */
            }
        }
    }

    sealed class UiEvent {
        object NavigateToOrderSuccess : UiEvent()
    }
}
