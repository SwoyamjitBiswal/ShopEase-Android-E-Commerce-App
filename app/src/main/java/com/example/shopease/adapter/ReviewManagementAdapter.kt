package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.data.Review
import com.example.shopease.ui.ReviewViewModel

class ReviewManagementAdapter(
    private val reviews: MutableList<Pair<String, Review>>,
    private val viewModel: ReviewViewModel
) : RecyclerView.Adapter<ReviewManagementAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_management_item, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val (productId, review) = reviews[position]
        holder.bind(productId, review)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val reviewTitle: TextView = itemView.findViewById(R.id.review_title)
        private val reviewAuthor: TextView = itemView.findViewById(R.id.review_author_name)
        // Corrected: The view in the XML is a Button, not an ImageButton
        private val deleteButton: Button = itemView.findViewById(R.id.delete_button)

        fun bind(productId: String, review: Review) {
            reviewTitle.text = review.comment
            reviewAuthor.text = "by ${review.userName}"

            deleteButton.setOnClickListener {
                viewModel.deleteReview(productId, review)
                Toast.makeText(itemView.context, "Review deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
