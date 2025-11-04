package com.example.shopease.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shopease.R
import com.google.android.material.button.MaterialButton

class AdminDashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manageProductsButton = view.findViewById<MaterialButton>(R.id.manage_products_button)
        val manageReviewsButton = view.findViewById<MaterialButton>(R.id.manage_reviews_button)

        manageProductsButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProductManagementFragment())
                .addToBackStack(null)
                .commit()
        }

        manageReviewsButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ReviewManagementFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
