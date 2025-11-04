package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopease.R
import com.example.shopease.ShopEaseApplication
import com.example.shopease.adapter.ReviewAdapter
import com.example.shopease.data.Product
import com.example.shopease.data.Review
import com.example.shopease.ui.CartViewModel
import com.example.shopease.ui.ViewModelFactory
import com.example.shopease.ui.WishlistViewModel
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class ProductDetailFragment : Fragment() {

    private lateinit var product: Product
    private lateinit var reviewsRecyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter

    private val cartViewModel: CartViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as ShopEaseApplication).container.shoppingRepository)
    }

    private val wishlistViewModel: WishlistViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as ShopEaseApplication).container.shoppingRepository)
    }

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
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productImageView: ImageView = view.findViewById(R.id.product_detail_image)
        val productNameView: TextView = view.findViewById(R.id.product_detail_name)
        val productPriceView: TextView = view.findViewById(R.id.product_detail_price)
        val productRatingBar: RatingBar = view.findViewById(R.id.product_detail_rating)
        val productDescriptionView: TextView = view.findViewById(R.id.product_detail_description)
        val addToCartButton: MaterialButton = view.findViewById(R.id.add_to_cart_button_detail)
        val wishlistButton: ImageButton = view.findViewById(R.id.wishlist_button)
        val writeReviewButton: MaterialButton = view.findViewById(R.id.write_review_button)

        reviewsRecyclerView = view.findViewById(R.id.reviews_recycler_view)
        reviewsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        productNameView.text = product.name
        productPriceView.text = String.format("$%.2f", product.price)
        productRatingBar.rating = product.rating
        productDescriptionView.text = "This is a great product!" // Placeholder
        Glide.with(this).load(product.imageUrl).into(productImageView)

        addToCartButton.setOnClickListener {
            cartViewModel.addProductToCart(product)
        }

        wishlistButton.setOnClickListener {
            wishlistViewModel.addProductToWishlist(product)
        }

        writeReviewButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, WriteReviewFragment.newInstance(product))
                .addToBackStack(null)
                .commit()
        }

        fetchReviews()
    }

    private fun fetchReviews() {
        val reviewsRef = FirebaseDatabase.getInstance().getReference("reviews").child(product.id.toString())
        reviewsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reviews = snapshot.children.mapNotNull { it.getValue(Review::class.java) }
                reviewAdapter = ReviewAdapter(reviews)
                reviewsRecyclerView.adapter = reviewAdapter
            }
            override fun onCancelled(error: DatabaseError) { /* Handle error */ }
        })
    }

    companion object {
        fun newInstance(product: Product) = ProductDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable("product", product)
            }
        }
    }
}
