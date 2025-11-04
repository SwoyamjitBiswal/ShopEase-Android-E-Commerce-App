package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopease.R
import com.example.shopease.data.HomeItem
import com.example.shopease.fragments.CategoryResultsFragment
import com.example.shopease.ui.CartViewModel
import com.example.shopease.ui.WishlistViewModel

class HomeAdapter(
    private val items: List<HomeItem>,
    private val cartViewModel: CartViewModel,
    private val wishlistViewModel: WishlistViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_BANNER = 0
        private const val TYPE_HEADER = 1
        private const val TYPE_CATEGORIES = 2
        private const val TYPE_PRODUCT_CAROUSEL = 3
        private const val TYPE_DIVIDER = 4
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is HomeItem.Banner -> TYPE_BANNER
            is HomeItem.Header -> TYPE_HEADER
            is HomeItem.Categories -> TYPE_CATEGORIES
            is HomeItem.ProductCarousel -> TYPE_PRODUCT_CAROUSEL
            is HomeItem.Divider -> TYPE_DIVIDER
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_BANNER -> BannerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_banner_item, parent, false))
            TYPE_HEADER -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_header_item, parent, false))
            TYPE_CATEGORIES -> CategoriesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_categories_item, parent, false))
            TYPE_PRODUCT_CAROUSEL -> ProductCarouselViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_carousel_item, parent, false))
            TYPE_DIVIDER -> DividerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_divider_item, parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BannerViewHolder -> holder.bind(items[position] as HomeItem.Banner)
            is HeaderViewHolder -> holder.bind(items[position] as HomeItem.Header)
            is CategoriesViewHolder -> holder.bind(items[position] as HomeItem.Categories)
            is ProductCarouselViewHolder -> holder.bind(items[position] as HomeItem.ProductCarousel)
            is DividerViewHolder -> { /* No binding needed */ }
        }
    }

    override fun getItemCount(): Int = items.size

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bannerImage: ImageView = itemView.findViewById(R.id.banner_image)
        fun bind(banner: HomeItem.Banner) {
            Glide.with(itemView.context).load(banner.imageUrl).into(bannerImage)
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerTitle: TextView = itemView.findViewById(R.id.header_title)
        private val seeAllButton: TextView = itemView.findViewById(R.id.see_all_button)
        fun bind(header: HomeItem.Header) {
            headerTitle.text = header.title
            seeAllButton.setOnClickListener {
                Toast.makeText(itemView.context, "See All clicked for ${header.title}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoriesRecyclerView: RecyclerView = itemView.findViewById(R.id.categories_recycler_view)

        fun bind(categories: HomeItem.Categories) {
            categoriesRecyclerView.adapter = CategoryAdapter(categories.categories) { category ->
                val activity = itemView.context as AppCompatActivity
                val fragment = CategoryResultsFragment.newInstance(category)
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    inner class ProductCarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productCarouselRecyclerView: RecyclerView = itemView.findViewById(R.id.carousel_recycler_view)
        fun bind(productCarousel: HomeItem.ProductCarousel) {
            productCarouselRecyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            productCarouselRecyclerView.adapter = ProductCarouselAdapter(productCarousel.products, cartViewModel, wishlistViewModel)
        }
    }

    class DividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
