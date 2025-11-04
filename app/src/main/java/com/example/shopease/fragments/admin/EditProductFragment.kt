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

class EditProductFragment : Fragment() {

    private val viewModel: AdminViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as ShopEaseApplication).container.shoppingRepository)
    }

    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            product = it.getParcelable("product")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameLayout = view.findViewById<TextInputLayout>(R.id.edit_product_name_layout)
        val priceLayout = view.findViewById<TextInputLayout>(R.id.edit_product_price_layout)
        val imageUrlLayout = view.findViewById<TextInputLayout>(R.id.edit_product_image_url_layout)
        val nameEditText = view.findViewById<TextInputEditText>(R.id.edit_product_name)
        val priceEditText = view.findViewById<TextInputEditText>(R.id.edit_product_price)
        val imageUrlEditText = view.findViewById<TextInputEditText>(R.id.edit_product_image_url)
        val categorySpinner = view.findViewById<Spinner>(R.id.edit_product_category)
        val updateButton = view.findViewById<MaterialButton>(R.id.update_product_button)

        // Pre-fill the fields
        nameEditText.setText(product.name)
        priceEditText.setText(product.price.toString())
        imageUrlEditText.setText(product.imageUrl)

        // Set up the category spinner
        val categories = listOf("Electronics", "Fashion", "Home & Kitchen", "Books", "Sports & Outdoors")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter
        categorySpinner.setSelection(categories.indexOf(product.category))

        updateButton.setOnClickListener {
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
                val updatedProduct = product.copy(name = name, price = price!!, imageUrl = imageUrl, category = category)
                viewModel.updateProduct(updatedProduct)

                Toast.makeText(requireContext(), "Product updated successfully", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
        }
    }

    companion object {
        fun newInstance(product: Product) = EditProductFragment().apply {
            arguments = Bundle().apply {
                putParcelable("product", product)
            }
        }
    }
}
