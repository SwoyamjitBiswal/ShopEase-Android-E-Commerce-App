package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.ShopEaseApplication
import com.example.shopease.adapter.WishlistAdapter
import com.example.shopease.ui.WishlistViewModel
import com.example.shopease.ui.ViewModelFactory
import kotlinx.coroutines.launch

class WishlistFragment : Fragment() {

    private lateinit var wishlistRecyclerView: RecyclerView
    private lateinit var wishlistAdapter: WishlistAdapter
    private lateinit var emptyWishlistView: LinearLayout

    private val viewModel: WishlistViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as ShopEaseApplication).container.shoppingRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wishlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wishlistRecyclerView = view.findViewById(R.id.wishlist_recycler_view)
        emptyWishlistView = view.findViewById(R.id.empty_wishlist_view)

        wishlistRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.wishlistUiState.collect { uiState ->
                    if (uiState.wishlistItems.isEmpty()) {
                        emptyWishlistView.visibility = View.VISIBLE
                        wishlistRecyclerView.visibility = View.GONE
                    } else {
                        emptyWishlistView.visibility = View.GONE
                        wishlistRecyclerView.visibility = View.VISIBLE
                        wishlistAdapter = WishlistAdapter(uiState.wishlistItems, viewModel)
                        wishlistRecyclerView.adapter = wishlistAdapter
                    }
                }
            }
        }
    }
}
