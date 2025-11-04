package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
import com.example.shopease.ui.ViewModelFactory
import com.example.shopease.ui.WishlistViewModel
import kotlinx.coroutines.launch

class CategoryResultsFragment : Fragment() {

    private lateinit var categoryResultsRecyclerView: RecyclerView
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

    private var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString("category")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryResultsRecyclerView = view.findViewById(R.id.category_results_recycler_view)
        noResultsMessage = view.findViewById(R.id.no_results_message)

        categoryResultsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Set the toolbar title
        (requireActivity() as AppCompatActivity).supportActionBar?.title = category

        viewModel.onSearchQueryChanged(category ?: "")

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchResults.collect { products ->
                    if (products.isEmpty()) {
                        noResultsMessage.visibility = View.VISIBLE
                        categoryResultsRecyclerView.visibility = View.GONE
                    } else {
                        noResultsMessage.visibility = View.GONE
                        categoryResultsRecyclerView.visibility = View.VISIBLE
                        productAdapter = ProductAdapter(products, cartViewModel, wishlistViewModel)
                        categoryResultsRecyclerView.adapter = productAdapter
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // Reset the toolbar title when leaving the fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
    }

    companion object {
        fun newInstance(category: String) = CategoryResultsFragment().apply {
            arguments = Bundle().apply {
                putString("category", category)
            }
        }
    }
}
