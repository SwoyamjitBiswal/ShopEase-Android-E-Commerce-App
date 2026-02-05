package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.shopease.R
import com.example.shopease.data.HomeItem
import com.example.shopease.data.Product
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeAdapter(
    private val activity: FragmentActivity,
    private val onAddToCartClicked: (Product) -> Unit,
    private val onWishlistClicked: (Product) -> Unit,
    private val onItemClicked: (Product) -> Unit
) : ListAdapter<HomeItem, RecyclerView.ViewHolder>(HomeItem.DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomeItem.Banner -> R.layout.home_banner_carousel_layout
            is HomeItem.Header -> R.layout.home_header_item
            is HomeItem.Categories -> R.layout.home_categories_item
            is HomeItem.ProductCarousel -> R.layout.home_carousel_item
            is HomeItem.Divider -> R.layout.home_divider_item
            is HomeItem.ProductGrid -> R.layout.home_product_grid_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.home_banner_carousel_layout -> BannerViewHolder(view)
            R.layout.home_header_item -> HeaderViewHolder(view)
            R.layout.home_categories_item -> CategoriesViewHolder(view)
            R.layout.home_carousel_item -> ProductCarouselViewHolder(view)
            R.layout.home_divider_item -> DividerViewHolder(view)
            R.layout.home_product_grid_item -> ProductGridViewHolder(view)
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is HomeItem.Banner -> (holder as BannerViewHolder).bind(item)
            is HomeItem.Header -> (holder as HeaderViewHolder).bind(item)
            is HomeItem.Categories -> (holder as CategoriesViewHolder).bind(item)
            is HomeItem.ProductCarousel -> (holder as ProductCarouselViewHolder).bind(item)
            is HomeItem.ProductGrid -> (holder as ProductGridViewHolder).bind(item)
            is HomeItem.Divider -> { /* No-op */ }
        }
    }

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val viewPager: ViewPager2 = itemView.findViewById(R.id.banner_view_pager)
        private val tabLayout: TabLayout = itemView.findViewById(R.id.banner_tab_layout)

        fun bind(banner: HomeItem.Banner) {
            val adapter = BannerCarouselAdapter(banner.imageUrls)
            viewPager.adapter = adapter
            TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.header_title)
        fun bind(header: HomeItem.Header) {
            title.text = header.title
        }
    }

    class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.categories_recycler_view)
        fun bind(categories: HomeItem.Categories) {
            recyclerView.layoutManager = GridLayoutManager(itemView.context, 3)
            val adapter = CategoryAdapter { /* Handle category click */ }
            recyclerView.adapter = adapter
            adapter.submitList(categories.categories)
        }
    }

    inner class ProductCarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.carousel_recycler_view)
        fun bind(carousel: HomeItem.ProductCarousel) {
            recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            val adapter = ProductAdapter(
                onAddToCartClicked = { onAddToCartClicked(it.originalProduct) },
                onWishlistClicked = { onWishlistClicked(it.originalProduct) },
                onItemClicked = { onItemClicked(it.originalProduct) }
            )
            recyclerView.adapter = adapter
            adapter.submitList(carousel.products)
        }
    }

    inner class ProductGridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.product_grid_recycler_view)
        fun bind(grid: HomeItem.ProductGrid) {
            recyclerView.layoutManager = GridLayoutManager(itemView.context, 2)
            val adapter = ProductAdapter(
                onAddToCartClicked = { onAddToCartClicked(it.originalProduct) },
                onWishlistClicked = { onWishlistClicked(it.originalProduct) },
                onItemClicked = { onItemClicked(it.originalProduct) }
            )
            recyclerView.adapter = adapter
            adapter.submitList(grid.products)
        }
    }

    class DividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
