package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopease.R
import com.example.shopease.data.Product
import com.example.shopease.fragments.ProductDetailFragment
import com.example.shopease.ui.CartViewModel
import com.example.shopease.ui.WishlistViewModel

class ProductAdapter(
    private val cartViewModel: CartViewModel,
    private val wishlistViewModel: WishlistViewModel
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productImage: ImageView = itemView.findViewById(R.id.product_image)
        private val productName: TextView = itemView.findViewById(R.id.product_name)
        private val productPrice: TextView = itemView.findViewById(R.id.product_price)
        private val addToCartButton: ImageButton = itemView.findViewById(R.id.add_to_cart_icon_button) // Corrected ID
        private val wishlistButton: ImageButton = itemView.findViewById(R.id.product_wishlist_button) // Corrected ID

        fun bind(product: Product) {
            productName.text = product.name
            productPrice.text = String.format("$%.2f", product.price)
            Glide.with(itemView.context).load(product.imageUrl).into(productImage)

            itemView.setOnClickListener {
                val activity = itemView.context as FragmentActivity
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProductDetailFragment.newInstance(product))
                    .addToBackStack(null)
                    .commit()
            }

            addToCartButton.setOnClickListener {
                cartViewModel.addProductToCart(product)
            }

            wishlistButton.setOnClickListener {
                wishlistViewModel.addProductToWishlist(product)
            }
        }
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}
