package com.example.shopease.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopease.ShopEaseApplication

// This is the single, definitive ViewModelFactory for the entire application.
// All ViewModel imports are now corrected to point to this package.
import com.example.shopease.viewmodels.AdminViewModel
import com.example.shopease.viewmodels.CartViewModel
import com.example.shopease.viewmodels.HomeViewModel
import com.example.shopease.viewmodels.OrderViewModel
import com.example.shopease.viewmodels.PaymentViewModel
import com.example.shopease.viewmodels.SearchViewModel
import com.example.shopease.viewmodels.ShippingAddressViewModel
import com.example.shopease.viewmodels.WishlistViewModel

class ViewModelFactory(private val application: ShopEaseApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = application.container.shoppingRepository

        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(WishlistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WishlistViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository) as T
        }
         if (modelClass.isAssignableFrom(ShippingAddressViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShippingAddressViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PaymentViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
