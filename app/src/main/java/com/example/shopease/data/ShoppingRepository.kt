package com.example.shopease.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ShoppingRepository(
    private val productDao: ProductDao,
    private val cartDao: CartDao,
    private val wishlistDao: WishlistDao,
    private val orderDao: OrderDao,
    private val shippingAddressDao: ShippingAddressDao,
    private val reviewDao: ReviewDao
) {

    private val firebaseDb = FirebaseDatabase.getInstance()
    private val userId: String? get() = FirebaseAuth.getInstance().currentUser?.uid

    val allProducts: Flow<List<Product>> = productDao.getAllProducts().distinctUntilChanged()
    val allCartItems: Flow<List<CartItem>> = cartDao.getAllCartItems().distinctUntilChanged()
    val allWishlistItems: Flow<List<WishlistItem>> = wishlistDao.getAllWishlistItems().distinctUntilChanged()
    val allOrders: Flow<List<Order>> = orderDao.getAllOrders().distinctUntilChanged()
    val allReviews: Flow<List<Review>> = reviewDao.getAllReviews().distinctUntilChanged()

    init {
        syncProductsFromFirebase()
        syncReviewsFromFirebase()
        listenForAuthChanges()
    }

    // Product Functions
    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
        firebaseDb.getReference("products").child(product.id.toString()).setValue(product)
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
        firebaseDb.getReference("products").child(product.id.toString()).setValue(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
        firebaseDb.getReference("products").child(product.id.toString()).removeValue()
    }

    // Cart Functions
    suspend fun addCartItem(cartItem: CartItem) {
        cartDao.insertCartItem(cartItem)
        userId?.let { firebaseDb.getReference("users").child(it).child("cart").child(cartItem.productId.toString()).setValue(cartItem) }
    }

    suspend fun updateCartItem(cartItem: CartItem) {
        cartDao.updateCartItem(cartItem)
        userId?.let { firebaseDb.getReference("users").child(it).child("cart").child(cartItem.productId.toString()).setValue(cartItem) }
    }

    suspend fun deleteCartItem(cartItem: CartItem) {
        cartDao.deleteCartItem(cartItem)
        userId?.let { firebaseDb.getReference("users").child(it).child("cart").child(cartItem.productId.toString()).removeValue() }
    }

    suspend fun clearCart() {
        cartDao.clearCart()
        userId?.let { firebaseDb.getReference("users").child(it).child("cart").removeValue() }
    }
    
    // Wishlist Functions
    suspend fun addWishlistItem(wishlistItem: WishlistItem) {
        wishlistDao.insertWishlistItem(wishlistItem)
        userId?.let { firebaseDb.getReference("users").child(it).child("wishlist").child(wishlistItem.productId.toString()).setValue(wishlistItem) }
    }

    suspend fun deleteWishlistItem(wishlistItem: WishlistItem) {
        wishlistDao.deleteWishlistItem(wishlistItem)
        userId?.let { firebaseDb.getReference("users").child(it).child("wishlist").child(wishlistItem.productId.toString()).removeValue() }
    }

    // Order Functions
    suspend fun insertOrder(order: Order) {
        orderDao.insertOrder(order)
        userId?.let { firebaseDb.getReference("users").child(it).child("orders").child(order.orderId).setValue(order) }
    }

    // Shipping Address Functions
    fun getAddressesForUser(): Flow<List<ShippingAddress>> {
        return shippingAddressDao.getAddressesForUser(userId ?: "")
    }

    suspend fun insertAddress(address: ShippingAddress) {
        val addressWithUser = address.copy(userId = userId ?: "")
        shippingAddressDao.insertAddress(addressWithUser)
        userId?.let { firebaseDb.getReference("users").child(it).child("shippingAddresses").child(addressWithUser.id).setValue(addressWithUser) }
    }

    suspend fun updateAddress(address: ShippingAddress) {
        shippingAddressDao.updateAddress(address)
        userId?.let { firebaseDb.getReference("users").child(it).child("shippingAddresses").child(address.id).setValue(address) }
    }

    suspend fun deleteAddress(address: ShippingAddress) {
        shippingAddressDao.deleteAddress(address)
        userId?.let { firebaseDb.getReference("users").child(it).child("shippingAddresses").child(address.id).removeValue() }
    }

    private fun syncProductsFromFirebase() {
        val productsRef = firebaseDb.getReference("products")
        productsRef.keepSynced(true)
        productsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                CoroutineScope(Dispatchers.IO).launch {
                    val products = snapshot.children.mapNotNull { it.getValue(Product::class.java) }
                    productDao.insertAll(products)
                }
            }
            override fun onCancelled(error: DatabaseError) { /* Handle error */ }
        })
    }

    private fun syncReviewsFromFirebase() {
        val reviewsRef = firebaseDb.getReference("reviews")
        reviewsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                CoroutineScope(Dispatchers.IO).launch {
                    val reviews = mutableListOf<Review>()
                    snapshot.children.forEach { productIdSnapshot ->
                        productIdSnapshot.children.forEach { reviewSnapshot ->
                            reviewSnapshot.getValue(Review::class.java)?.let { review ->
                                reviews.add(review.copy(productId = productIdSnapshot.key ?: ""))
                            }
                        }
                    }
                    reviewDao.insertAll(reviews)
                }
            }
            override fun onCancelled(error: DatabaseError) { /* Handle error */ }
        })
    }

    private fun listenForAuthChanges() {
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            if (auth.currentUser != null) {
                attachUserSyncListeners(firebaseDb.getReference("users").child(auth.currentUser!!.uid))
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    cartDao.clearCart()
                    wishlistDao.clearWishlist()
                    orderDao.clearOrders()
                }
            }
        }
    }

    private fun attachUserSyncListeners(userRef: com.google.firebase.database.DatabaseReference) {
        // ... (user sync listeners should be implemented here to sync cart, wishlist, orders etc. from Firebase)
    }

    suspend fun submitReview(productId: String, review: Review) {
        val reviewId = firebaseDb.getReference("reviews").child(productId).push().key ?: ""
        val reviewWithId = review.copy(id = reviewId, productId = productId)
        firebaseDb.getReference("reviews").child(productId).child(reviewId).setValue(reviewWithId)
    }
}
