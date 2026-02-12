package com.example.shopease.data

sealed class HomeItem {
    data class ProductItem(val product: Product) : HomeItem()
    data class HeaderItem(val title: String) : HomeItem()
}
