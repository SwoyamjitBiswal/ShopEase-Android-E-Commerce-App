package com.example.shopease.data

object OrderManager {
    private val _orders = mutableListOf<Order>()
    val orders: List<Order> = _orders

    fun addOrder(order: Order) {
        _orders.add(order)
    }
}
