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
import com.example.shopease.data.Product
import com.example.shopease.viewmodels.CartUiEvent
import com.example.shopease.viewmodels.CartViewModel
import com.example.shopease.viewmodels.SearchViewModel
import com.example.shopease.viewmodels.ViewModelFactory
import com.example.shopease.viewmodels.WishlistViewModel
import kotlinx.coroutines.launch

class CategoryResultsFragment : Fragment() {

    private lateinit var productAdapter: ProductAdapter

    private val viewModel: SearchViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application as ShopEaseApplication)
    }
    private val cartViewModel: CartViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application as ShopEaseApplication)
    }
    private val wishlistViewModel: WishlistViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application as ShopEaseApplication)
    }

    private var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(ARG_CATEGORY)
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

        val categoryResultsRecyclerView: RecyclerView = view.findViewById(R.id.category_results_recycler_view)
        val noResultsMessage: TextView = view.findViewById(R.id.no_results_message)

        productAdapter = ProductAdapter(
            onAddToCartClicked = { productUiModel -> cartViewModel.onEvent(CartUiEvent.AddItem(productUiModel.originalProduct)) },
            onWishlistClicked = { productUiModel -> wishlistViewModel.toggleWishlist(productUiModel.originalProduct) },
            onItemClicked = { productUiModel -> openProductDetail(productUiModel.originalProduct) }
        )
        categoryResultsRecyclerView.adapter = productAdapter
        categoryResultsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

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
                        productAdapter.submitList(products)
                    }
                }
            }
        }
    }

    private fun openProductDetail(product: Product) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ProductDetailFragment.newInstance(product))
            .addToBackStack(null)
            .commit()
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
    }

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String) = CategoryResultsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_CATEGORY, category)
            }
        }
    }
}
