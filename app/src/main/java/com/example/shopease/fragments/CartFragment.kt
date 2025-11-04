package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.ShopEaseApplication
import com.example.shopease.adapter.CartAdapter
import com.example.shopease.ui.CartViewModel
import com.example.shopease.ui.ViewModelFactory
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var totalPrice: TextView
    private lateinit var checkoutButton: Button

    private val viewModel: CartViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as ShopEaseApplication).container.shoppingRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartRecyclerView = view.findViewById(R.id.cart_recycler_view)
        totalPrice = view.findViewById(R.id.total_price)
        checkoutButton = view.findViewById(R.id.checkout_button)

        cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartUiState.collect { uiState ->
                    cartAdapter = CartAdapter(uiState.cartItems.toMutableList(), viewModel)
                    cartRecyclerView.adapter = cartAdapter
                    totalPrice.text = String.format("Total: $%.2f", uiState.totalPrice)
                }
            }
        }

        checkoutButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PaymentFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
