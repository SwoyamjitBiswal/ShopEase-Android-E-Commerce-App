package com.example.shopease.data

import androidx.recyclerview.widget.DiffUtil
import com.example.shopease.model.ProductUiModel

/**
 * A sealed interface representing all possible UI components on the home screen.
 */
sealed interface HomeItem {
    /** Represents a large, scrolling promotional banner. */
    data class Banner(val imageUrls: List<String>) : HomeItem

    /** Represents a simple text header for a section (e.g., "Featured Products"). */
    data class Header(val title: String) : HomeItem

    /** Represents a list of categories. */
    data class Categories(val categories: List<Category>) : HomeItem

    /** Represents a horizontally scrolling carousel of products. */
    data class ProductCarousel(val products: List<ProductUiModel>) : HomeItem

    /** Represents a simple visual divider. */
    object Divider : HomeItem

    /** Represents a grid of products. */
    data class ProductGrid(val products: List<ProductUiModel>) : HomeItem

    class DiffCallback : DiffUtil.ItemCallback<HomeItem>() {
        override fun areItemsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
            return when {
                oldItem is Header && newItem is Header -> oldItem.title == newItem.title
                oldItem is Banner && newItem is Banner -> oldItem.imageUrls == newItem.imageUrls
                // Add other specific checks if needed
                else -> oldItem == newItem // Fallback for simple objects
            }
        }

        override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
            return oldItem == newItem
        }
    }
}
