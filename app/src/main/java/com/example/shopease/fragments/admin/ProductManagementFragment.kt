package com.example.shopease.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.ShopEaseApplication
import com.example.shopease.adapter.ProductManagementAdapter
import com.example.shopease.ui.AdminViewModel
import com.example.shopease.ui.ViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ProductManagementFragment : Fragment() {

    private lateinit var productManagementRecyclerView: RecyclerView
    private lateinit var productManagementAdapter: ProductManagementAdapter
    private lateinit var addProductFab: FloatingActionButton

    private val viewModel: AdminViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as ShopEaseApplication).container.shoppingRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_management, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productManagementRecyclerView = view.findViewById(R.id.product_management_recycler_view)
        addProductFab = view.findViewById(R.id.add_product_fab)

        productManagementRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.adminUiState.collect { uiState ->
                    productManagementAdapter = ProductManagementAdapter(uiState.productList.toMutableList(), viewModel)
                    productManagementRecyclerView.adapter = productManagementAdapter
                }
            }
        }

        addProductFab.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddProductFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}