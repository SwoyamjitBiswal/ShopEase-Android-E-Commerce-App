package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopease.R
import com.example.shopease.data.WishlistItemWithProduct
import com.google.android.material.button.MaterialButton
import java.util.Locale

class WishlistAdapter(
    private val onRemoveClicked: (WishlistItemWithProduct) -> Unit,
    private val onItemClicked: (WishlistItemWithProduct) -> Unit
) : ListAdapter<WishlistItemWithProduct, WishlistAdapter.WishlistViewHolder>(WishlistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wishlist_item, parent, false)
        return WishlistViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onRemoveClicked, onItemClicked)
    }

    class WishlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wishlistItemImage: ImageView = itemView.findViewById(R.id.wishlist_item_image)
        private val wishlistItemName: TextView = itemView.findViewById(R.id.wishlist_item_name)
        private val wishlistItemPrice: TextView = itemView.findViewById(R.id.wishlist_item_price)
        private val removeFromWishlistButton: MaterialButton = itemView.findViewById(R.id.remove_from_wishlist_button)

        fun bind(
            item: WishlistItemWithProduct,
            onRemoveClicked: (WishlistItemWithProduct) -> Unit,
            onItemClicked: (WishlistItemWithProduct) -> Unit // Corrected
        ) {
            wishlistItemName.text = item.product.name
            wishlistItemPrice.text = String.format(Locale.US, "$%.2f", item.product.price)
            Glide.with(itemView.context).load(item.product.imageUrl).into(wishlistItemImage)

            itemView.setOnClickListener { onItemClicked(item) }
            removeFromWishlistButton.setOnClickListener { onRemoveClicked(item) }
        }
    }
}

private class WishlistDiffCallback : DiffUtil.ItemCallback<WishlistItemWithProduct>() {
    override fun areItemsTheSame(oldItem: WishlistItemWithProduct, newItem: WishlistItemWithProduct): Boolean {
        return oldItem.wishlistItem.productId == newItem.wishlistItem.productId
    }

    override fun areContentsTheSame(oldItem: WishlistItemWithProduct, newItem: WishlistItemWithProduct): Boolean {
        return oldItem == newItem
    }
}
