package com.example.shopease.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.shopease.R
import com.example.shopease.ShopEaseApplication
import com.example.shopease.data.Product
import com.example.shopease.ui.AdminViewModel
import com.example.shopease.ui.ViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AddProductFragment : Fragment() {

    private val viewModel: AdminViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as ShopEaseApplication).container.shoppingRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameLayout = view.findViewById<TextInputLayout>(R.id.add_product_name_layout)
        val priceLayout = view.findViewById<TextInputLayout>(R.id.add_product_price_layout)
        val imageUrlLayout = view.findViewById<TextInputLayout>(R.id.add_product_image_url_layout)
        val nameEditText = view.findViewById<TextInputEditText>(R.id.add_product_name)
        val priceEditText = view.findViewById<TextInputEditText>(R.id.add_product_price)
        val imageUrlEditText = view.findViewById<TextInputEditText>(R.id.add_product_image_url)
        val categorySpinner = view.findViewById<Spinner>(R.id.add_product_category)
        val addButton = view.findViewById<MaterialButton>(R.id.add_product_button)

        // Set up the category spinner
        val categories = listOf("Electronics", "Fashion", "Home & Kitchen", "Books", "Sports & Outdoors")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        addButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val price = priceEditText.text.toString().toDoubleOrNull()
            val imageUrl = imageUrlEditText.text.toString().trim()
            val category = categorySpinner.selectedItem.toString()

            var isValid = true
            if (name.isEmpty()) {
                nameLayout.error = "Product name is required"
                isValid = false
            } else {
                nameLayout.error = null
            }

            if (price == null) {
                priceLayout.error = "Invalid price"
                isValid = false
            } else {
                priceLayout.error = null
            }

            if (imageUrl.isEmpty()) {
                imageUrlLayout.error = "Image URL is required"
                isValid = false
            } else {
                imageUrlLayout.error = null
            }

            if (isValid) {
                val newProduct = Product(name = name, price = price!!, imageUrl = imageUrl, category = category)
                viewModel.addProduct(newProduct)

                Toast.makeText(requireContext(), "Product added successfully", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
        }
    }
}
