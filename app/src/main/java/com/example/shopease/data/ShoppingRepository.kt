package com.example.shopease.data

class ShoppingRepository(
    private val productDao: ProductDao,
    private val cartDao: CartDao,
    private val wishlistDao: WishlistDao
) {
    // Product-related methods
    fun getAllProducts() = productDao.getAllProducts()

    // Cart-related methods
    fun getAllCartItems() = cartDao.getAllCartItems()
    suspend fun insertCartItem(item: CartItem) = cartDao.insert(item)
    suspend fun updateCartItem(item: CartItem) = cartDao.update(item)
    suspend fun deleteCartItem(item: CartItem) = cartDao.delete(item)
    suspend fun clearCart() = cartDao.clearCart()

    // Wishlist-related methods
    fun getAllWishlistItems() = wishlistDao.getWishlistItemsWithProducts()
    suspend fun insertWishlistItem(item: WishlistItem) = wishlistDao.insertWishlistItem(item)
    suspend fun deleteWishlistItem(item: WishlistItem) = wishlistDao.deleteWishlistItem(item)
}
