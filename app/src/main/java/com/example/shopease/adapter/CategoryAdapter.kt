package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R

class CategoryAdapter(
    private val onCategoryClick: (String) -> Unit
) : ListAdapter<String, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryName: TextView = itemView.findViewById(R.id.category_name)
        private val categoryIcon: ImageView = itemView.findViewById(R.id.category_icon)
        private val categoryBackground: ConstraintLayout = itemView.findViewById(R.id.category_background)

        fun bind(category: String) {
            categoryName.text = category
            itemView.setOnClickListener { onCategoryClick(category) }

            val iconResId = when (category) {
                "Books" -> R.drawable.ic_category_books
                "Electronics" -> R.drawable.ic_category_electronics
                "Fashion" -> R.drawable.ic_category_fashion
                "Home & Kitchen" -> R.drawable.ic_category_home
                "Sports & Outdoors" -> R.drawable.ic_category_sports
                else -> R.drawable.ic_category
            }
            categoryIcon.setImageResource(iconResId)

            val backgroundResId = when (category) {
                "Books" -> R.drawable.category_bg_books
                "Electronics" -> R.drawable.category_bg_electronics
                "Fashion" -> R.drawable.category_bg_fashion
                "Home & Kitchen" -> R.drawable.category_bg_home
                "Sports & Outdoors" -> R.drawable.category_bg_sports
                else -> R.drawable.category_card_background
            }
            categoryBackground.setBackgroundResource(backgroundResId)
        }
    }
}

class CategoryDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
