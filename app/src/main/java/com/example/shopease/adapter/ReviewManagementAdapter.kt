package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.data.Review

class ReviewManagementAdapter(
    private val onDeleteClicked: (Review) -> Unit
) : ListAdapter<Review, ReviewManagementAdapter.ReviewViewHolder>(ReviewDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_management_item, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position), onDeleteClicked)
    }

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val reviewText: TextView = itemView.findViewById(R.id.review_comment) // Corrected ID
        private val reviewRating: TextView = itemView.findViewById(R.id.review_rating)
        private val reviewUser: TextView = itemView.findViewById(R.id.review_author_name) // Corrected ID
        private val deleteButton: Button = itemView.findViewById(R.id.delete_button) // Corrected ID

        fun bind(review: Review, onDeleteClicked: (Review) -> Unit) {
            reviewText.text = review.comment
            reviewRating.text = review.rating.toString()
            reviewUser.text = review.userName
            deleteButton.setOnClickListener { onDeleteClicked(review) }
        }
    }
}

private class ReviewDiffCallback : DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }
}
