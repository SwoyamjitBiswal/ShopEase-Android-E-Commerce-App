package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopease.R
import com.example.shopease.data.Product
import com.example.shopease.ui.CartViewModel

class CartAdapter(
    private val cartItems: MutableList<Pair<Product, Int>>,
    private val viewModel: CartViewModel
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val (product, quantity) = cartItems[position]
        holder.bind(product, quantity)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cartItemImage: ImageView = itemView.findViewById(R.id.cart_item_image)
        private val cartItemName: TextView = itemView.findViewById(R.id.cart_item_name)
        private val cartItemPrice: TextView = itemView.findViewById(R.id.cart_item_price)
        private val cartItemQuantity: TextView = itemView.findViewById(R.id.cart_item_quantity)
        private val removeButton: ImageButton = itemView.findViewById(R.id.remove_item_button)
        private val incrementButton: ImageButton = itemView.findViewById(R.id.increment_quantity_button)
        private val decrementButton: ImageButton = itemView.findViewById(R.id.decrement_quantity_button)

        fun bind(product: Product, quantity: Int) {
            cartItemName.text = product.name
            cartItemPrice.text = String.format("$%.2f", product.price)
            cartItemQuantity.text = quantity.toString()
            Glide.with(itemView.context).load(product.imageUrl).into(cartItemImage)

            removeButton.setOnClickListener {
                viewModel.removeProductFromCart(product)
            }

            incrementButton.setOnClickListener {
                viewModel.incrementQuantity(product)
            }

            decrementButton.setOnClickListener {
                viewModel.decrementQuantity(product)
            }
        }
    }
}