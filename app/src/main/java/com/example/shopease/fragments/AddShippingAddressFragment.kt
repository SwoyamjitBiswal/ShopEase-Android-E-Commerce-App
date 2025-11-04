package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.shopease.R
import com.example.shopease.ShopEaseApplication
import com.example.shopease.data.ShippingAddress
import com.example.shopease.ui.ShippingAddressViewModel
import com.example.shopease.ui.ViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class AddShippingAddressFragment : Fragment() {

    private val viewModel: ShippingAddressViewModel by viewModels {
        ViewModelFactory((requireActivity().application as ShopEaseApplication).container.shoppingRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_shipping_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fullNameEditText = view.findViewById<TextInputEditText>(R.id.full_name_edit_text)
        val addressLine1EditText = view.findViewById<TextInputEditText>(R.id.address_line_1_edit_text)
        val addressLine2EditText = view.findViewById<TextInputEditText>(R.id.address_line_2_edit_text)
        val cityEditText = view.findViewById<TextInputEditText>(R.id.city_edit_text)
        val stateEditText = view.findViewById<TextInputEditText>(R.id.state_edit_text)
        val zipCodeEditText = view.findViewById<TextInputEditText>(R.id.zip_code_edit_text)
        val phoneNumberEditText = view.findViewById<TextInputEditText>(R.id.phone_number_edit_text)
        val saveAddressButton = view.findViewById<MaterialButton>(R.id.save_address_button)

        saveAddressButton.setOnClickListener {
            val address = ShippingAddress(
                fullName = fullNameEditText.text.toString(),
                addressLine1 = addressLine1EditText.text.toString(),
                addressLine2 = addressLine2EditText.text.toString(),
                city = cityEditText.text.toString(),
                state = stateEditText.text.toString(),
                zipCode = zipCodeEditText.text.toString(),
                phoneNumber = phoneNumberEditText.text.toString()
            )

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.insertAddress(address)
                Toast.makeText(requireContext(), "Address saved", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
        }
    }
}
