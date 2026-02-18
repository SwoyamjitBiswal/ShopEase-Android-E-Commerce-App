package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.shopease.ShopEaseApplication
import com.example.shopease.databinding.FragmentCategoryResultsBinding
import com.example.shopease.viewmodels.HomeViewModel
import com.example.shopease.viewmodels.ViewModelFactory

class CategoryResultsFragment : Fragment() {

    private var _binding: FragmentCategoryResultsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        val application = requireActivity().application as ShopEaseApplication
        ViewModelFactory(application.container.shoppingRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Add your logic here
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
