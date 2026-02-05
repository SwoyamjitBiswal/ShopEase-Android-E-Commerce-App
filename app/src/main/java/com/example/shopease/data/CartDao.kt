package com.example.shopease.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartItem)

    @Update
    suspend fun update(item: CartItem)

    @Delete
    suspend fun delete(item: CartItem)

    @Transaction
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItemWithProduct>>

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
