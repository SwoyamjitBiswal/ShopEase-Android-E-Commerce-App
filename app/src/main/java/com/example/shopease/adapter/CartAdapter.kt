package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopease.R
import com.example.shopease.data.CartItemWithProduct
import java.util.Locale

class CartAdapter(
    private val onItemRemoved: (CartItemWithProduct) -> Unit,
    private val onQuantityChanged: (CartItemWithProduct, Int) -> Unit
) : ListAdapter<CartItemWithProduct, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemRemoved, onQuantityChanged)
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productImage: ImageView = itemView.findViewById(R.id.cart_item_image)
        private val productName: TextView = itemView.findViewById(R.id.cart_item_name)
        private val productPrice: TextView = itemView.findViewById(R.id.cart_item_price)
        private val quantityText: TextView = itemView.findViewById(R.id.cart_item_quantity)
        private val removeButton: ImageButton = itemView.findViewById(R.id.remove_item_button)
        private val incrementButton: ImageButton = itemView.findViewById(R.id.increment_quantity_button)
        private val decrementButton: ImageButton = itemView.findViewById(R.id.decrement_quantity_button)

        fun bind(item: CartItemWithProduct, onItemRemoved: (CartItemWithProduct) -> Unit, onQuantityChanged: (CartItemWithProduct, Int) -> Unit) {
            productName.text = item.product.name
            productPrice.text = String.format(Locale.US, "$%.2f", item.product.price)
            quantityText.text = item.cartItem.quantity.toString()
            Glide.with(itemView.context).load(item.product.imageUrl).into(productImage)

            removeButton.setOnClickListener { onItemRemoved(item) }
            incrementButton.setOnClickListener { onQuantityChanged(item, item.cartItem.quantity + 1) }
            decrementButton.setOnClickListener { 
                if (item.cartItem.quantity > 1) {
                    onQuantityChanged(item, item.cartItem.quantity - 1) 
                }
            }
        }
    }
}

private class CartDiffCallback : DiffUtil.ItemCallback<CartItemWithProduct>() {
    override fun areItemsTheSame(oldItem: CartItemWithProduct, newItem: CartItemWithProduct): Boolean {
        return oldItem.cartItem.id == newItem.cartItem.id
    }

    override fun areContentsTheSame(oldItem: CartItemWithProduct, newItem: CartItemWithProduct): Boolean {
        return oldItem == newItem
    }
}
