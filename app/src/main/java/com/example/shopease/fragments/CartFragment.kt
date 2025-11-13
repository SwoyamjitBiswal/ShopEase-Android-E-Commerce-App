package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.ShopEaseApplication
import com.example.shopease.adapter.CartAdapter
import com.example.shopease.ui.CartUiEvent
import com.example.shopease.ui.CartViewModel
import com.example.shopease.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var totalAmountTextView: TextView
    private lateinit var emptyCartView: LinearLayout
    private lateinit var checkoutLayout: LinearLayout

    private val viewModel: CartViewModel by viewModels {
        ViewModelFactory(requireActivity().application as ShopEaseApplication)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        totalAmountTextView = view.findViewById(R.id.total_price)
        emptyCartView = view.findViewById(R.id.empty_cart_view)
        checkoutLayout = view.findViewById(R.id.checkout_layout)
        setupRecyclerView(view)

        val checkoutButton: Button = view.findViewById(R.id.checkout_button)
        checkoutButton.setOnClickListener {
            viewModel.onEvent(CartUiEvent.PlaceOrder)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.cartItems.collect { cartItems ->
                        cartAdapter.submitList(cartItems)
                        val total = cartItems.sumOf { it.price * it.quantity }
                        totalAmountTextView.text = String.format("Total: $%.2f", total)

                        if (cartItems.isEmpty()) {
                            emptyCartView.visibility = View.VISIBLE
                            checkoutLayout.visibility = View.GONE
                        } else {
                            emptyCartView.visibility = View.GONE
                            checkoutLayout.visibility = View.VISIBLE
                        }
                    }
                }

                launch {
                    viewModel.uiEvent.collect { event ->
                        when (event) {
                            is CartViewModel.UiEvent.NavigateToOrderSuccess -> {
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, OrderSuccessFragment())
                                    .commit()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView(view: View) {
        cartRecyclerView = view.findViewById(R.id.cart_recycler_view)
        cartAdapter = CartAdapter(
            onItemRemoved = { item -> viewModel.onEvent(CartUiEvent.RemoveItem(item)) },
            onQuantityChanged = { item, quantity -> viewModel.onEvent(CartUiEvent.UpdateQuantity(item, quantity)) }
        )
        cartRecyclerView.adapter = cartAdapter
        cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
