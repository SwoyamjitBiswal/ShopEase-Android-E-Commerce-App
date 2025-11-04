package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.shopease.R
import com.example.shopease.ShopEaseApplication
import com.example.shopease.ui.CartViewModel
import com.example.shopease.ui.ViewModelFactory
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class PaymentFragment : Fragment() {

    private val cartViewModel: CartViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as ShopEaseApplication).container.shoppingRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val placeOrderButton: MaterialButton = view.findViewById(R.id.place_order_button)

        placeOrderButton.setOnClickListener {
            if (cartViewModel.cartUiState.value.cartItems.isEmpty()) {
                Toast.makeText(requireContext(), "Your cart is empty.", Toast.LENGTH_SHORT).show()
            } else {
                cartViewModel.placeOrder()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.orderPlacedSuccessfully.collect { isSuccess ->
                    if (isSuccess) {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, OrderSuccessfulFragment())
                            .commit()
                        cartViewModel.onOrderPlaced()
                    }
                }
            }
        }
    }
}