package com.example.shopease.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ShoppingRepository(
    private val productDao: ProductDao
    // DEBUGGING: Temporarily disabling all other DAOs
    /*
    private val cartDao: CartDao,
    private val wishlistDao: WishlistDao,
    private val orderDao: OrderDao,
    private val shippingAddressDao: ShippingAddressDao,
    private val reviewDao: ReviewDao,
    private val paymentMethodDao: PaymentMethodDao,
    private val categoryDao: CategoryDao
    */
) {

    private val firebaseDb = FirebaseDatabase.getInstance()
    private val userId: String? get() = FirebaseAuth.getInstance().currentUser?.uid

    val allProducts: Flow<List<Product>> = productDao.getAllProducts().distinctUntilChanged()
    // DEBUGGING: Temporarily disabling all other Flows
    /*
    val allCartItemsWithProducts: Flow<List<CartItemWithProduct>> = cartDao.getCartItemsWithProducts().distinctUntilChanged()
    val allWishlistItemsWithProducts: Flow<List<WishlistItemWithProduct>> = wishlistDao.getWishlistItemsWithProducts().distinctUntilChanged()
    val allOrders: Flow<List<Order>> = orderDao.getAllOrders().distinctUntilChanged()
    val allReviews: Flow<List<Review>> = reviewDao.getAllReviews().distinctUntilChanged()
    val allPaymentMethods: Flow<List<PaymentMethod>> = paymentMethodDao.getAllPaymentMethods().distinctUntilChanged()
    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories().distinctUntilChanged()
    */

    init {
        syncProductsFromFirebase()
        // DEBUGGING: Temporarily disabling all other sync calls
        /*
        syncReviewsFromFirebase()
        syncCategoriesFromFirebase()
        listenForAuthChanges()
        */
    }

    // ... (All other functions will be temporarily removed for debugging)

    private fun syncProductsFromFirebase() {
        val productsRef = firebaseDb.getReference("products")
        productsRef.keepSynced(true)
        productsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                CoroutineScope(Dispatchers.IO).launch {
                    val products = snapshot.children.mapNotNull { child ->
                        try {
                            val map = child.getValue<Map<String, Any>>() ?: return@mapNotNull null
                            Product(
                                id = map["id"].toString(),
                                name = map["name"]?.toString() ?: "",
                                description = map["description"]?.toString() ?: "",
                                price = (map["price"] as? Number)?.toDouble() ?: 0.0,
                                imageUrl = map["imageUrl"]?.toString() ?: "",
                                category = map["category"]?.toString() ?: "",
                                stock = (map["stock"] as? Long)?.toInt() ?: 0,
                                rating = (map["rating"] as? Number)?.toFloat() ?: 0.0f
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    productDao.insertAll(products)
                }
            }
            override fun onCancelled(error: DatabaseError) { /* Handle error */ }
        })
    }
}
