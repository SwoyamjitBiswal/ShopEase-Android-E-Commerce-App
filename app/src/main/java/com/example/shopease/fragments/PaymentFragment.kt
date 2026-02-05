package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.shopease.R
import com.example.shopease.ShopEaseApplication
import com.example.shopease.viewmodels.CartUiEvent
import com.example.shopease.viewmodels.CartViewModel
import com.example.shopease.viewmodels.ViewModelFactory
import java.util.Locale
import kotlinx.coroutines.launch

class PaymentFragment : Fragment() {

    private val cartViewModel: CartViewModel by viewModels {
        ViewModelFactory(requireActivity().application as ShopEaseApplication)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val totalAmountTextView: TextView = view.findViewById(R.id.total_amount_text_view)
        val placeOrderButton: Button = view.findViewById(R.id.place_order_button)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.cartItems.collect { cartItems ->
                    // Correctly access nested properties to calculate total
                    val total = cartItems.sumOf { it.product.price * it.cartItem.quantity }
                    totalAmountTextView.text = String.format(Locale.US, "Total: $%.2f", total)
                }
            }
        }

        placeOrderButton.setOnClickListener {
            cartViewModel.onEvent(CartUiEvent.PlaceOrder)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.uiEvent.collect { event ->
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
