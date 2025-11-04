package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopease.R
import com.example.shopease.data.Product
import com.example.shopease.ui.WishlistViewModel
import com.google.android.material.button.MaterialButton

class WishlistAdapter(
    private val wishlistItems: List<Product>,
    private val viewModel: WishlistViewModel
) : RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wishlist_item, parent, false)
        return WishlistViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val product = wishlistItems[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return wishlistItems.size
    }

    inner class WishlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wishlistItemImage: ImageView = itemView.findViewById(R.id.wishlist_item_image)
        private val wishlistItemName: TextView = itemView.findViewById(R.id.wishlist_item_name)
        private val wishlistItemPrice: TextView = itemView.findViewById(R.id.wishlist_item_price)
        private val removeFromWishlistButton: MaterialButton = itemView.findViewById(R.id.remove_from_wishlist_button)

        fun bind(product: Product) {
            wishlistItemName.text = product.name
            wishlistItemPrice.text = String.format("$%.2f", product.price)
            Glide.with(itemView.context).load(product.imageUrl).into(wishlistItemImage)

            removeFromWishlistButton.setOnClickListener {
                viewModel.removeProductFromWishlist(product)
            }
        }
    }
}
