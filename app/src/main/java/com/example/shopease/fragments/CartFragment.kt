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
import com.example.shopease.data.CartRepository
import com.example.shopease.viewmodel.CartViewModel
import com.example.shopease.viewmodel.CartViewModelFactory
import java.util.Locale
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var totalAmountTextView: TextView
    private lateinit var emptyCartView: LinearLayout
    private lateinit var checkoutLayout: LinearLayout

    private val viewModel: CartViewModel by viewModels {
        val application = requireActivity().application as ShopEaseApplication
        CartViewModelFactory(CartRepository(application.database.cartDao()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        totalAmountTextView = view.findViewById(R.id.tv_total_price)
        emptyCartView = view.findViewById(R.id.empty_cart_view)
        checkoutLayout = view.findViewById(R.id.checkout_bar)
        setupRecyclerView(view)

        val checkoutButton: Button = view.findViewById(R.id.btn_proceed_to_buy)
        checkoutButton.setOnClickListener {
            // Add your navigation logic here
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allCartItems.collect { cartItems ->
                    cartAdapter.submitList(cartItems)
                    val total = cartItems.sumOf { it.product.price * it.cartItem.quantity }
                    totalAmountTextView.text = String.format(Locale.US, "$%.2f", total)

                    if (cartItems.isEmpty()) {
                        emptyCartView.visibility = View.VISIBLE
                        checkoutLayout.visibility = View.GONE
                    } else {
                        emptyCartView.visibility = View.GONE
                        checkoutLayout.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupRecyclerView(view: View) {
        cartRecyclerView = view.findViewById(R.id.rv_cart_items)
        cartAdapter = CartAdapter(
            onItemRemoved = { item -> viewModel.delete(item.cartItem) },
            onQuantityChanged = { item, quantity ->
                item.cartItem.quantity = quantity
                viewModel.update(item.cartItem)
            }
        )
        cartRecyclerView.adapter = cartAdapter
        cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
