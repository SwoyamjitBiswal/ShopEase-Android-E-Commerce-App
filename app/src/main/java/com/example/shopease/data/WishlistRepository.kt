package com.example.shopease.data

import kotlinx.coroutines.flow.Flow

class WishlistRepository(private val wishlistDao: WishlistDao) {

    fun getAllWishlistItems(): Flow<List<WishlistItemWithProduct>> = wishlistDao.getWishlistItemsWithProducts()

    suspend fun insert(item: WishlistItem) {
        wishlistDao.insertWishlistItem(item)
    }

    suspend fun delete(item: WishlistItem) {
        wishlistDao.deleteWishlistItem(item)
    }
}
