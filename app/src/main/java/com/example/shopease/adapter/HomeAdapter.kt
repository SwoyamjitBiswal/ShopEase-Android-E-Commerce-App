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
import com.example.shopease.data.HomeItem
import com.example.shopease.data.Product

class HomeAdapter(
    private val onAddToCartClicked: (Product) -> Unit,
    private val onWishlistClicked: (Product) -> Unit,
    private val onItemClicked: (Product) -> Unit
) : ListAdapter<HomeItem, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomeItem.HeaderItem -> R.layout.item_header
            is HomeItem.ProductItem -> R.layout.item_product_grid
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_header -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false))
            R.layout.item_product_grid -> ProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_product_grid, parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is HomeItem.HeaderItem -> (holder as HeaderViewHolder).bind(item)
            is HomeItem.ProductItem -> (holder as ProductViewHolder).bind(item.product, onAddToCartClicked, onWishlistClicked, onItemClicked)
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerTitle: TextView = itemView.findViewById(R.id.tv_header_title)

        fun bind(header: HomeItem.HeaderItem) {
            headerTitle.text = header.title
        }
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productImage: ImageView = itemView.findViewById(R.id.iv_product_image)
        private val productName: TextView = itemView.findViewById(R.id.tv_product_title)
        private val productPrice: TextView = itemView.findViewById(R.id.tv_product_price)
        private val addToCartButton: ImageView = itemView.findViewById(R.id.btn_add_to_cart)

        fun bind(product: Product, onAddToCartClicked: (Product) -> Unit, onWishlistClicked: (Product) -> Unit, onItemClicked: (Product) -> Unit) {
            productName.text = product.name
            productPrice.text = "$${product.price}"
            Glide.with(itemView.context).load(product.imageUrl).into(productImage)

            itemView.setOnClickListener { onItemClicked(product) }
            addToCartButton.setOnClickListener { onAddToCartClicked(product) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<HomeItem>() {
        override fun areItemsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
            return if (oldItem is HomeItem.ProductItem && newItem is HomeItem.ProductItem) {
                oldItem.product.id == newItem.product.id
            } else if (oldItem is HomeItem.HeaderItem && newItem is HomeItem.HeaderItem) {
                oldItem.title == newItem.title
            } else {
                false
            }
        }

        override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
            return oldItem == newItem
        }
    }
}
