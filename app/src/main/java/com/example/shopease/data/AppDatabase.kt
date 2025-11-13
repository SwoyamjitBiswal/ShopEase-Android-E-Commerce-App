package com.example.shopease.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        Product::class, 
        CartItem::class, 
        WishlistItem::class, 
        Order::class, 
        ShippingAddress::class, 
        Review::class,
        PaymentMethod::class
    ],
    version = 2, // Incremented version
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun orderDao(): OrderDao
    abstract fun shippingAddressDao(): ShippingAddressDao
    abstract fun reviewDao(): ReviewDao
    abstract fun paymentMethodDao(): PaymentMethodDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "shopease_database")
                    .fallbackToDestructiveMigration() // Important for schema changes
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
