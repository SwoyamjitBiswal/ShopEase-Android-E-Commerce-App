package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.adapter.CategoryAdapter
import com.example.shopease.data.Category

class CategoriesFragment : Fragment() {

    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesRecyclerView = view.findViewById(R.id.categories_recycler_view)
        categoriesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val categories = listOf(
            Category("1", "Electronics", ""),
            Category("2", "Fashion", ""),
            Category("3", "Home & Kitchen", ""),
            Category("4", "Books", ""),
            Category("5", "Sports & Outdoors", ""),
            Category("6", "Beauty", ""),
            Category("7", "Toys & Games", ""),
            Category("8", "Automotive", "")
        )

        categoryAdapter = CategoryAdapter { category ->
            // Handle category click
        }
        categoriesRecyclerView.adapter = categoryAdapter
        categoryAdapter.submitList(categories)
    }
}
