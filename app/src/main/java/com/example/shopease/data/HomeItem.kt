package com.example.shopease.data

sealed class HomeItem {
    data class Banner(val imageUrl: String) : HomeItem()
    data class Header(val title: String) : HomeItem()
    data class Categories(val categories: List<String>) : HomeItem()
    data class ProductCarousel(val products: List<Product>) : HomeItem()
    data class ProductGrid(val products: List<Product>) : HomeItem()
    object Divider : HomeItem()
}
