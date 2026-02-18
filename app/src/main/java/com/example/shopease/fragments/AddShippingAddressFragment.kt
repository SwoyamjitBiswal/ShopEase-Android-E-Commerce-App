package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.shopease.ShopEaseApplication
import com.example.shopease.databinding.FragmentAddShippingAddressBinding
import com.example.shopease.viewmodels.ShippingAddressViewModel
import com.example.shopease.viewmodels.ViewModelFactory

class AddShippingAddressFragment : Fragment() {

    private var _binding: FragmentAddShippingAddressBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShippingAddressViewModel by viewModels {
        val application = requireActivity().application as ShopEaseApplication
        ViewModelFactory(application.container.shoppingRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddShippingAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveAddressButton.setOnClickListener {
            // Add your logic to add a new shipping address
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
