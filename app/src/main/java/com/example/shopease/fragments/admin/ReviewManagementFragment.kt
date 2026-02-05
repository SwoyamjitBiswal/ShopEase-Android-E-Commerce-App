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
import com.example.shopease.adapter.ReviewManagementAdapter
import com.example.shopease.viewmodels.AdminViewModel
import com.example.shopease.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

class ReviewManagementFragment : Fragment() {

    private lateinit var reviewManagementRecyclerView: RecyclerView
    private lateinit var reviewManagementAdapter: ReviewManagementAdapter

    private val viewModel: AdminViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application as ShopEaseApplication)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_review_management, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reviewManagementRecyclerView = view.findViewById(R.id.reviews_recycler_view)
        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.adminUiState.collect { uiState ->
                    reviewManagementAdapter.submitList(uiState.reviewList)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        reviewManagementAdapter = ReviewManagementAdapter {
            viewModel.deleteReview(it)
        }
        reviewManagementRecyclerView.adapter = reviewManagementAdapter
        reviewManagementRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
