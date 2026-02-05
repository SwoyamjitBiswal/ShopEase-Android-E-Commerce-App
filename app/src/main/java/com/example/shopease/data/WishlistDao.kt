package com.example.shopease.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {

    @Transaction // Required for relational queries
    @Query("SELECT * FROM wishlist_items")
    fun getWishlistItemsWithProducts(): Flow<List<WishlistItemWithProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlistItem(wishlistItem: WishlistItem)

    @Delete
    suspend fun deleteWishlistItem(wishlistItem: WishlistItem)
    
    @Query("DELETE FROM wishlist_items")
    suspend fun clearWishlist()
}
