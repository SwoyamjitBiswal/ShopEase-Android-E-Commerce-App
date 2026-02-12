package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.ShopEaseApplication
import com.example.shopease.adapter.HomeAdapter
import com.example.shopease.data.CartItem
import com.example.shopease.data.Product
import com.example.shopease.viewmodel.CartViewModel
import com.example.shopease.viewmodel.HomeViewModel
import com.example.shopease.viewmodel.WishlistViewModel
import com.example.shopease.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var homeRecyclerView: RecyclerView
    private lateinit var homeAdapter: HomeAdapter

    private val homeViewModel: HomeViewModel by viewModels {
        val application = requireActivity().application as ShopEaseApplication
        ViewModelFactory(application.container.shoppingRepository)
    }
    private val cartViewModel: CartViewModel by viewModels {
        val application = requireActivity().application as ShopEaseApplication
        ViewModelFactory(application.container.shoppingRepository)
    }
    private val wishlistViewModel: WishlistViewModel by viewModels {
        val application = requireActivity().application as ShopEaseApplication
        ViewModelFactory(application.container.shoppingRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.homeItems.collect { homeItems ->
                    homeAdapter.submitList(homeItems)
                }
            }
        }
    }

    private fun setupRecyclerView(view: View) {
        homeRecyclerView = view.findViewById(R.id.rv_products)
        homeAdapter = HomeAdapter(
            onAddToCartClicked = { product ->
                val cartItem = CartItem(productId = product.id, quantity = 1)
                cartViewModel.insert(cartItem)
            },
            onWishlistClicked = { product ->
                wishlistViewModel.toggleWishlist(product)
            },
            onItemClicked = { product ->
                openProductDetail(product)
            }
        )
        homeRecyclerView.adapter = homeAdapter
        homeRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun openProductDetail(product: Product) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ProductDetailFragment.newInstance(product))
            .addToBackStack(null)
            .commit()
    }
}
