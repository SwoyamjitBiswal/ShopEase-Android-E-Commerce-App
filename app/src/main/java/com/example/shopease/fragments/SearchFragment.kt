package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.shopease.R
import com.example.shopease.ShopEaseApplication
import com.example.shopease.ui.SearchViewModel
import com.example.shopease.ui.ViewModelFactory
import com.google.android.material.chip.Chip

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as ShopEaseApplication).container.shoppingRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = view.findViewById<SearchView>(R.id.search_view_main)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.onSearchQueryChanged(query ?: "")
                navigateToResults()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        // Set click listeners for the chips
        view.findViewById<Chip>(R.id.chip_headphones).setOnClickListener { performSearchFromChip("Headphones") }
        view.findViewById<Chip>(R.id.chip_shoes).setOnClickListener { performSearchFromChip("Running Shoes") }
        view.findViewById<Chip>(R.id.chip_smartwatch).setOnClickListener { performSearchFromChip("Smartwatch") }
        view.findViewById<Chip>(R.id.chip_backpack).setOnClickListener { performSearchFromChip("Backpack") }
        view.findViewById<Chip>(R.id.chip_camera).setOnClickListener { performSearchFromChip("Camera") }
    }

    private fun performSearchFromChip(query: String) {
        viewModel.onSearchQueryChanged(query)
        navigateToResults()
    }

    private fun navigateToResults() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SearchResultsFragment())
            .addToBackStack(null)
            .commit()
    }
}
