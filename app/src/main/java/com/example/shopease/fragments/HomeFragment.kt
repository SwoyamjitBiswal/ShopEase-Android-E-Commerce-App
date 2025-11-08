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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.ShopEaseApplication
import com.example.shopease.adapter.CategoryAdapter
import com.example.shopease.adapter.ProductAdapter
import com.example.shopease.ui.CartViewModel
import com.example.shopease.ui.HomeViewModel
import com.example.shopease.ui.ViewModelFactory
import com.example.shopease.ui.WishlistViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter

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

        setupViews(view)
        setupAdapters()
        setupSearch(view)
        observeViewModel()
    }

    private fun setupViews(view: View) {
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView)
        productRecyclerView = view.findViewById(R.id.productRecyclerView)
        categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        productRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun setupAdapters() {
        categoryAdapter = CategoryAdapter { category ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CategoryResultsFragment.newInstance(category))
                .addToBackStack(null)
                .commit()
        }
        categoryRecyclerView.adapter = categoryAdapter

        productAdapter = ProductAdapter(cartViewModel, wishlistViewModel)
        productRecyclerView.adapter = productAdapter
    }

    private fun setupSearch(view: View) {
        val searchEditText = view.findViewById<TextInputEditText>(R.id.searchEditText)
        searchEditText.isFocusable = false
        searchEditText.isClickable = true
        searchEditText.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeUiState.collect { uiState ->
                    categoryAdapter.submitList(uiState.categories)
                    productAdapter.submitList(uiState.allProducts)
                }
            }
        }
    }
}
