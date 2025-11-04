package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopease.R
import com.example.shopease.data.Review

class ReviewAdapter(private val reviews: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userImage: ImageView = itemView.findViewById(R.id.review_user_image)
        private val userName: TextView = itemView.findViewById(R.id.review_user_name)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.review_rating)
        private val comment: TextView = itemView.findViewById(R.id.review_comment)

        fun bind(review: Review) {
            userName.text = review.userName
            ratingBar.rating = review.rating
            comment.text = review.comment
            Glide.with(itemView.context).load(review.userImageUrl).placeholder(R.drawable.ic_person).into(userImage)
        }
    }
}
