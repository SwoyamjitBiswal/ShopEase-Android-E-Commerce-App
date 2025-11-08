package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.ShopEaseApplication
import com.example.shopease.adapter.ProductAdapter
import com.example.shopease.ui.CartViewModel
import com.example.shopease.ui.SearchViewModel
import com.example.shopease.ui.SortOrder
import com.example.shopease.ui.ViewModelFactory
import com.example.shopease.ui.WishlistViewModel
import kotlinx.coroutines.launch

class SearchResultsFragment : Fragment() {

    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var noResultsMessage: TextView

    private val viewModel: SearchViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as ShopEaseApplication).container.shoppingRepository)
    }

    private val cartViewModel: CartViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as ShopEaseApplication).container.shoppingRepository)
    }

    private val wishlistViewModel: WishlistViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as ShopEaseApplication).container.shoppingRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchResultsRecyclerView = view.findViewById(R.id.search_results_recycler_view)
        noResultsMessage = view.findViewById(R.id.no_results_message)

        // Create the adapter only once
        productAdapter = ProductAdapter(cartViewModel, wishlistViewModel)
        searchResultsRecyclerView.adapter = productAdapter
        searchResultsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchResults.collect { products ->
                    if (products.isEmpty()) {
                        noResultsMessage.visibility = View.VISIBLE
                        searchResultsRecyclerView.visibility = View.GONE
                    } else {
                        noResultsMessage.visibility = View.GONE
                        searchResultsRecyclerView.visibility = View.VISIBLE
                        // Use submitList for efficient updates
                        productAdapter.submitList(products)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sort_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_by_relevance -> viewModel.onSortOrderChanged(SortOrder.RELEVANCE)
            R.id.sort_by_price_low_to_high -> viewModel.onSortOrderChanged(SortOrder.PRICE_LOW_TO_HIGH)
            R.id.sort_by_price_high_to_low -> viewModel.onSortOrderChanged(SortOrder.PRICE_HIGH_TO_LOW)
            R.id.sort_by_rating -> viewModel.onSortOrderChanged(SortOrder.RATING)
        }
        return super.onOptionsItemSelected(item)
    }
}
