package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopease.R
import com.example.shopease.model.ProductUiModel

class ProductCarouselAdapter(
    private val onAddToCartClicked: (ProductUiModel) -> Unit,
    private val onWishlistClicked: (ProductUiModel) -> Unit,
    private val onItemClicked: (ProductUiModel) -> Unit
) : ListAdapter<ProductUiModel, ProductCarouselAdapter.ProductViewHolder>(ProductCarouselDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_carousel_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product, onAddToCartClicked, onWishlistClicked, onItemClicked)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.product_name)
        private val productPrice: TextView = itemView.findViewById(R.id.product_price)
        private val productImage: ImageView = itemView.findViewById(R.id.product_image)
        private val productRating: RatingBar = itemView.findViewById(R.id.product_rating)
        private val productWishlistButton: ImageButton = itemView.findViewById(R.id.product_wishlist_button)
        private val addToCartButton: ImageButton = itemView.findViewById(R.id.add_to_cart_icon_button)

        fun bind(
            product: ProductUiModel,
            onAddToCartClicked: (ProductUiModel) -> Unit,
            onWishlistClicked: (ProductUiModel) -> Unit,
            onItemClicked: (ProductUiModel) -> Unit
        ) {
            productName.text = product.name
            productPrice.text = String.format("$%.2f", product.price)
            productRating.rating = product.rating
            Glide.with(itemView.context).load(product.imageUrl).into(productImage)

            itemView.setOnClickListener { onItemClicked(product) }
            addToCartButton.setOnClickListener { onAddToCartClicked(product) }
            productWishlistButton.setOnClickListener { onWishlistClicked(product) }

            val wishIcon = if (product.isWished) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            productWishlistButton.setImageResource(wishIcon)
        }
    }
}

private class ProductCarouselDiffCallback : DiffUtil.ItemCallback<ProductUiModel>() {
    override fun areItemsTheSame(oldItem: ProductUiModel, newItem: ProductUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductUiModel, newItem: ProductUiModel): Boolean {
        return oldItem == newItem
    }
}
