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
import com.example.shopease.model.ProductUiModel

class ProductAdapter(
    private val onAddToCartClicked: (ProductUiModel) -> Unit,
    private val onWishlistClicked: (ProductUiModel) -> Unit,
    private val onItemClicked: (ProductUiModel) -> Unit
) : ListAdapter<ProductUiModel, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product, onAddToCartClicked, onWishlistClicked, onItemClicked)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productImage: ImageView = itemView.findViewById(R.id.product_image)
        private val productName: TextView = itemView.findViewById(R.id.product_name)
        private val productPrice: TextView = itemView.findViewById(R.id.product_price)
        private val addToCartButton: ImageButton = itemView.findViewById(R.id.add_to_cart_icon_button)
        private val wishlistButton: ImageButton = itemView.findViewById(R.id.product_wishlist_button)

        fun bind(
            product: ProductUiModel,
            onAddToCartClicked: (ProductUiModel) -> Unit,
            onWishlistClicked: (ProductUiModel) -> Unit,
            onItemClicked: (ProductUiModel) -> Unit
        ) {
            productName.text = product.name
            productPrice.text = String.format("$%.2f", product.price)
            Glide.with(itemView.context).load(product.imageUrl).into(productImage)

            addToCartButton.setOnClickListener { onAddToCartClicked(product) }
            wishlistButton.setOnClickListener { onWishlistClicked(product) }
            itemView.setOnClickListener { onItemClicked(product) }

            val wishIcon = if (product.isWished) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            wishlistButton.setImageResource(wishIcon)
        }
    }
}

// Keep only this declaration of ProductDiffCallback
private class ProductDiffCallback : DiffUtil.ItemCallback<ProductUiModel>() {
    override fun areItemsTheSame(oldItem: ProductUiModel, newItem: ProductUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductUiModel, newItem: ProductUiModel): Boolean {
        return oldItem == newItem
    }
}
