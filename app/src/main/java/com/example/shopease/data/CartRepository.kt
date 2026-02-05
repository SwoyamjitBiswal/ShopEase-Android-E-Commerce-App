package com.example.shopease.data

import kotlinx.coroutines.flow.Flow

class CartRepository(private val cartDao: CartDao) {

    fun getAllCartItems(): Flow<List<CartItemWithProduct>> = cartDao.getAllCartItems()

    suspend fun insert(item: CartItem) {
        cartDao.insert(item)
    }

    suspend fun update(item: CartItem) {
        cartDao.update(item)
    }

    suspend fun delete(item: CartItem) {
        cartDao.delete(item)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }
}
