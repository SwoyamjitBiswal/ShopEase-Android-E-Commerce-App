package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.data.Order
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class OrderAdapter(private val orders: List<Order>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val orderId: TextView = itemView.findViewById(R.id.order_id)
        private val orderDate: TextView = itemView.findViewById(R.id.order_date)
        private val orderTotal: TextView = itemView.findViewById(R.id.order_total)
        private val orderItemsCount: TextView = itemView.findViewById(R.id.order_items_count)

        fun bind(order: Order) {
            orderId.text = "Order #${order.orderId.take(8)}..."
            
            val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.getDefault())
            val instant = order.date.toInstant()
            val zonedDateTime = instant.atZone(ZoneId.systemDefault())
            orderDate.text = formatter.format(zonedDateTime)
            
            orderTotal.text = String.format("Total: $%.2f", order.total)
            orderItemsCount.text = "Items: ${order.items.size}"
        }
    }
}
