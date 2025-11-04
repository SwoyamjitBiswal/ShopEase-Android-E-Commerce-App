package com.example.shopease.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.CartItem
import com.example.shopease.data.Order
import com.example.shopease.data.Product
import com.example.shopease.data.ShoppingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.UUID

data class CartUiState(
    val cartItems: List<Pair<Product, Int>> = emptyList(),
    val totalPrice: Double = 0.0
)

class CartViewModel(private val repository: ShoppingRepository) : ViewModel() {

    private val _orderPlacedSuccessfully = MutableStateFlow(false)
    val orderPlacedSuccessfully: StateFlow<Boolean> = _orderPlacedSuccessfully.asStateFlow()

    val cartUiState: StateFlow<CartUiState> = combine(
        repository.allProducts,
        repository.allCartItems
    ) { products, cartItems ->
        val itemsWithProducts = cartItems.mapNotNull { cartItem ->
            products.find { it.id == cartItem.productId }?.let { it to cartItem.quantity }
        }
        val totalPrice = itemsWithProducts.sumOf { (product, quantity) -> product.price * quantity }
        CartUiState(itemsWithProducts, totalPrice)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CartUiState()
    )

    fun addProductToCart(product: Product) {
        viewModelScope.launch {
            val existingCartItem = cartUiState.value.cartItems.find { it.first.id == product.id }
            if (existingCartItem != null) {
                repository.updateCartItem(CartItem(product.id, existingCartItem.second + 1))
            } else {
                repository.addCartItem(CartItem(product.id, 1))
            }
        }
    }

    fun removeProductFromCart(product: Product) {
        viewModelScope.launch {
            repository.deleteCartItem(CartItem(product.id, 1))
        }
    }

    fun incrementQuantity(product: Product) {
        viewModelScope.launch {
            val existingCartItem = cartUiState.value.cartItems.find { it.first.id == product.id }
            if (existingCartItem != null) {
                repository.updateCartItem(CartItem(product.id, existingCartItem.second + 1))
            }
        }
    }

    fun decrementQuantity(product: Product) {
        viewModelScope.launch {
            val existingCartItem = cartUiState.value.cartItems.find { it.first.id == product.id }
            if (existingCartItem != null && existingCartItem.second > 1) {
                repository.updateCartItem(CartItem(product.id, existingCartItem.second - 1))
            } else if (existingCartItem != null) {
                removeProductFromCart(product)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch { repository.clearCart() }
    }

    fun placeOrder() {
        viewModelScope.launch {
            val cartState = cartUiState.value
            if (cartState.cartItems.isNotEmpty()) {
                val order = withContext(Dispatchers.Default) {
                    Order(
                        orderId = UUID.randomUUID().toString(),
                        items = cartState.cartItems.map { (product, quantity) ->
                            CartItem(product.id, quantity)
                        },
                        total = cartState.totalPrice,
                        date = Date()
                    )
                }
                repository.insertOrder(order)
                clearCart()
                _orderPlacedSuccessfully.value = true
            }
        }
    }

    fun onOrderPlaced() {
        _orderPlacedSuccessfully.value = false
    }
}