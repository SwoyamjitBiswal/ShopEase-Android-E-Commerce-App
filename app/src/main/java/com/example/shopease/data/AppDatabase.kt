package com.example.shopease.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        Product::class,
        CartItem::class
        // DEBUGGING: Temporarily disabling all other entities to isolate the crash
        /*
        WishlistItem::class, 
        Order::class, 
        ShippingAddress::class, 
        Review::class,
        PaymentMethod::class,
        Category::class
        */
    ],
    version = 19, // Incremented for cart feature
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao

    /*
    // DEBUGGING: Temporarily disabling all other DAOs
    abstract fun wishlistDao(): WishlistDao
    abstract fun orderDao(): OrderDao
    abstract fun shippingAddressDao(): ShippingAddressDao
    abstract fun reviewDao(): ReviewDao
    abstract fun paymentMethodDao(): PaymentMethodDao
    abstract fun categoryDao(): CategoryDao
    */

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "shopease_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
