package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.ShopEaseApplication
import com.example.shopease.adapter.CategoryAdapter
import com.example.shopease.adapter.HomeAdapter
import com.example.shopease.data.HomeItem
import com.example.shopease.ui.CartViewModel
import com.example.shopease.ui.HomeViewModel
import com.example.shopease.ui.ViewModelFactory
import com.example.shopease.ui.WishlistViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var homeAdapter: HomeAdapter

    private val viewModel: HomeViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as ShopEaseApplication).container.shoppingRepository)
    }
    private val cartViewModel: CartViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as ShopEaseApplication).container.shoppingRepository)
    }
    private val wishlistViewModel: WishlistViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as ShopEaseApplication).container.shoppingRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryRecyclerView: RecyclerView = view.findViewById(R.id.categoryRecyclerView)
        val productRecyclerView: RecyclerView = view.findViewById(R.id.productRecyclerView)

        // Setup for Category RecyclerView
        categoryAdapter = CategoryAdapter { category ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CategoryResultsFragment.newInstance(category))
                .addToBackStack(null)
                .commit()
        }
        categoryRecyclerView.adapter = categoryAdapter
        categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Setup for the main content RecyclerView
        productRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeUiState.collect { uiState ->
                    // Submit categories to the category adapter
                    categoryAdapter.submitList(uiState.categories)

                    // Create and set the adapter for the main content
                    val homeItems = listOf(
                        HomeItem.Header("Deals of the Day"),
                        HomeItem.ProductCarousel(uiState.dealsOfTheDay),
                        HomeItem.Divider,
                        HomeItem.Header("New Arrivals"),
                        HomeItem.ProductGrid(uiState.newArrivals)
                    )
                    homeAdapter = HomeAdapter(homeItems, cartViewModel, wishlistViewModel)
                    productRecyclerView.adapter = homeAdapter
                }
            }
        }

        val searchEditText: TextInputEditText = view.findViewById(R.id.searchEditText)
        searchEditText.isFocusable = false
        searchEditText.isClickable = true
        searchEditText.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
