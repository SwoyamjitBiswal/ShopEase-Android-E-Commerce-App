package com.example.shopease.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.Review
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReviewViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val reviewsRef = database.getReference("reviews")

    private val _reviews = MutableStateFlow<List<Pair<String, Review>>>(emptyList())
    val reviews: StateFlow<List<Pair<String, Review>>> = _reviews.asStateFlow()

    init {
        fetchReviews()
    }

    private fun fetchReviews() {
        reviewsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reviewList = mutableListOf<Pair<String, Review>>()
                for (productSnapshot in snapshot.children) {
                    for (reviewSnapshot in productSnapshot.children) {
                        val review = reviewSnapshot.getValue(Review::class.java)
                        review?.let {
                            reviewList.add(productSnapshot.key!! to it)
                        }
                    }
                }
                _reviews.value = reviewList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun deleteReview(productId: String, review: Review) {
        viewModelScope.launch {
            reviewsRef.child(productId).orderByChild("comment").equalTo(review.comment)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (reviewSnapshot in snapshot.children) {
                            reviewSnapshot.ref.removeValue()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })
        }
    }
}
