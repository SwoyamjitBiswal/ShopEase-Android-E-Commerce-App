package com.example.shopease.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShippingAddressDao {

    @Query("SELECT * FROM shipping_addresses WHERE userId = :userId")
    fun getAddressesForUser(userId: String): Flow<List<ShippingAddress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: ShippingAddress)

    @Update
    suspend fun updateAddress(address: ShippingAddress)

    @Delete
    suspend fun deleteAddress(address: ShippingAddress)
}
