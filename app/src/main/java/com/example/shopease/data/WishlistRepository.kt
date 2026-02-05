package com.example.shopease.data

import kotlinx.coroutines.flow.Flow

class WishlistRepository(private val wishlistDao: WishlistDao) {

    fun getAllWishlistItems(): Flow<List<WishlistItemWithProduct>> = wishlistDao.getAllWishlistItems()

    suspend fun insert(item: WishlistItem) {
        wishlistDao.insert(item)
    }

    suspend fun delete(item: WishlistItem) {
        wishlistDao.delete(item)
    }
}
