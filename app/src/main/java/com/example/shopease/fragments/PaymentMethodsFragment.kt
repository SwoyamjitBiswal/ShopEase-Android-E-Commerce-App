package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.ShopEaseApplication
import com.example.shopease.adapter.PaymentMethodsAdapter
import com.example.shopease.data.PaymentMethod
import com.example.shopease.viewmodels.PaymentViewModel
import com.example.shopease.viewmodels.ViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class PaymentMethodsFragment : Fragment() {

    private lateinit var paymentMethodsRecyclerView: RecyclerView
    private lateinit var noPaymentMethodsText: TextView
    private lateinit var adapter: PaymentMethodsAdapter

    private val viewModel: PaymentViewModel by viewModels {
        ViewModelFactory(requireActivity().application as ShopEaseApplication)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment_methods, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }

        paymentMethodsRecyclerView = view.findViewById(R.id.payment_methods_recycler_view)
        noPaymentMethodsText = view.findViewById(R.id.no_payment_methods_text)
        val addPaymentMethodFab: FloatingActionButton = view.findViewById(R.id.add_payment_method_fab)

        setupRecyclerView()

        addPaymentMethodFab.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddPaymentMethodFragment())
                .addToBackStack(null)
                .commit()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.paymentMethods.collect { paymentMethods ->
                    updateUI(paymentMethods)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = PaymentMethodsAdapter { paymentMethod ->
            viewModel.deletePaymentMethod(paymentMethod)
            Toast.makeText(requireContext(), "Deleted ${paymentMethod.cardType}", Toast.LENGTH_SHORT).show()
        }
        paymentMethodsRecyclerView.adapter = adapter
        paymentMethodsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun updateUI(paymentMethods: List<PaymentMethod>) {
        adapter.submitList(paymentMethods)
        if (paymentMethods.isEmpty()) {
            noPaymentMethodsText.visibility = View.VISIBLE
            paymentMethodsRecyclerView.visibility = View.GONE
        } else {
            noPaymentMethodsText.visibility = View.GONE
            paymentMethodsRecyclerView.visibility = View.VISIBLE
        }
    }
}
