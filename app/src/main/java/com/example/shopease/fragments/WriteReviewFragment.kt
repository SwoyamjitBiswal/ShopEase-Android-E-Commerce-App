package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shopease.R
import com.example.shopease.data.Product
import com.example.shopease.data.Review
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class WriteReviewFragment : Fragment() {

    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            product = it.getParcelable("product")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_write_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ratingBar = view.findViewById<RatingBar>(R.id.submit_review_rating)
        val commentEditText = view.findViewById<TextInputEditText>(R.id.submit_review_comment)
        val submitButton = view.findViewById<MaterialButton>(R.id.submit_review_button)

        submitButton.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                Toast.makeText(requireContext(), "You must be logged in to write a review.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val reviewsRef = FirebaseDatabase.getInstance().getReference("reviews").child(product.id)
            val reviewId = reviewsRef.push().key ?: ""

            // Correctly create the Review object with all required fields
            val review = Review(
                id = reviewId,
                productId = product.id,
                userId = user.uid,
                userName = user.displayName ?: "Anonymous",
                userImageUrl = user.photoUrl?.toString() ?: "", // Handle nullable URL
                rating = ratingBar.rating,
                comment = commentEditText.text.toString(),
                date = System.currentTimeMillis() // Use Long for timestamp
            )

            reviewsRef.child(reviewId).setValue(review).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateProductRating()
                    Toast.makeText(requireContext(), "Review submitted successfully!", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Failed to submit review.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateProductRating() {
        val reviewsRef = FirebaseDatabase.getInstance().getReference("reviews").child(product.id)
        reviewsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reviews = snapshot.children.mapNotNull { it.getValue(Review::class.java) }
                val averageRating = if (reviews.isNotEmpty()) reviews.map { it.rating }.average().toFloat() else 0f
                FirebaseDatabase.getInstance().getReference("products").child(product.id).child("rating").setValue(averageRating)
            }
            override fun onCancelled(error: DatabaseError) { /* Handle error */ }
        })
    }

    companion object {
        fun newInstance(product: Product) = WriteReviewFragment().apply {
            arguments = Bundle().apply {
                putParcelable("product", product)
            }
        }
    }
}
