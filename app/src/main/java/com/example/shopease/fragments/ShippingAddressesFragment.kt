package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.ShopEaseApplication
import com.example.shopease.adapter.ShippingAddressAdapter
import com.example.shopease.ui.ShippingAddressViewModel
import com.example.shopease.ui.ViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ShippingAddressesFragment : Fragment() {

    private val viewModel: ShippingAddressViewModel by viewModels {
        ViewModelFactory((requireActivity().application as ShopEaseApplication).container.shoppingRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shipping_addresses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.shipping_addresses_recycler_view)
        val fab = view.findViewById<FloatingActionButton>(R.id.add_shipping_address_fab)

        val adapter = ShippingAddressAdapter { address ->
            // Handle address click - e.g., navigate to an edit screen
            Toast.makeText(requireContext(), "Clicked on ${address.fullName}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.shippingAddresses.collect { addresses ->
                    adapter.submitList(addresses)
                }
            }
        }

        fab.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddShippingAddressFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
