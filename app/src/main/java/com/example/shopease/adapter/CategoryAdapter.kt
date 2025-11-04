package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R

class CategoryAdapter(
    private val categories: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryIcon: ImageView = itemView.findViewById(R.id.category_icon)
        private val categoryName: TextView = itemView.findViewById(R.id.category_name)

        fun bind(category: String) {
            categoryName.text = category
            val iconRes = when (category) {
                "Electronics" -> R.drawable.ic_category_electronics
                "Fashion" -> R.drawable.ic_category_fashion
                "Home & Kitchen" -> R.drawable.ic_category_home
                "Books" -> R.drawable.ic_category_books
                "Sports & Outdoors" -> R.drawable.ic_category_sports
                else -> R.drawable.ic_add
            }
            categoryIcon.setImageResource(iconRes)
            itemView.setOnClickListener { onItemClick(category) }
        }
    }
}
