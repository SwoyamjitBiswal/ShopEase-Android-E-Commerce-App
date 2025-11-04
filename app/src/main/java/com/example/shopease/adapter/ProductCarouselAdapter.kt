package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopease.R
import com.example.shopease.data.Product
import com.example.shopease.fragments.ProductDetailFragment
import com.example.shopease.ui.CartViewModel
import com.example.shopease.ui.WishlistViewModel

class ProductCarouselAdapter(
    private var productList: List<Product>,
    private val cartViewModel: CartViewModel,
    private val wishlistViewModel: WishlistViewModel
) : RecyclerView.Adapter<ProductCarouselAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_carousel_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            val activity = it.context as AppCompatActivity
            val fragment = ProductDetailFragment.newInstance(product)
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.product_name)
        private val productPrice: TextView = itemView.findViewById(R.id.product_price)
        private val productImage: ImageView = itemView.findViewById(R.id.product_image)
        private val productRating: RatingBar = itemView.findViewById(R.id.product_rating)
        private val productWishlistButton: ImageButton = itemView.findViewById(R.id.product_wishlist_button)
        private val addToCartButton: ImageButton = itemView.findViewById(R.id.add_to_cart_icon_button)

        fun bind(product: Product) {
            productName.text = product.name
            productPrice.text = "$${product.price}"
            productRating.rating = product.rating
            Glide.with(itemView.context).load(product.imageUrl).into(productImage)

            updateWishlistButton(product)

            productWishlistButton.setOnClickListener {
                val isWishlisted = wishlistViewModel.isWishlisted(product, wishlistViewModel.wishlistUiState.value)
                if (isWishlisted) {
                    wishlistViewModel.removeProductFromWishlist(product)
                } else {
                    wishlistViewModel.addProductToWishlist(product)
                }
            }

            addToCartButton.setOnClickListener {
                cartViewModel.addProductToCart(product)
                Toast.makeText(itemView.context, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
            }
        }

        private fun updateWishlistButton(product: Product) {
            val isWishlisted = wishlistViewModel.isWishlisted(product, wishlistViewModel.wishlistUiState.value)
            if (isWishlisted) {
                productWishlistButton.setColorFilter(ContextCompat.getColor(itemView.context, R.color.colorAccent))
            } else {
                productWishlistButton.colorFilter = null
            }
        }
    }
}
