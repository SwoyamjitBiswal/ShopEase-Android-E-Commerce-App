package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.adapter.PaymentMethodAdapter
import com.example.shopease.data.PaymentMethod
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PaymentMethodsFragment : Fragment() {

    private lateinit var paymentMethodsRecyclerView: RecyclerView
    private lateinit var paymentMethodAdapter: PaymentMethodAdapter
    private lateinit var addNewPaymentMethodButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment_methods, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentMethodsRecyclerView = view.findViewById(R.id.payment_methods_recycler_view)
        addNewPaymentMethodButton = view.findViewById(R.id.add_new_payment_method_button)

        paymentMethodsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val paymentMethods = generateSamplePaymentMethods()
        paymentMethodAdapter = PaymentMethodAdapter(paymentMethods)
        paymentMethodsRecyclerView.adapter = paymentMethodAdapter

        addNewPaymentMethodButton.setOnClickListener {
            Toast.makeText(requireContext(), "Coming Soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateSamplePaymentMethods(): List<PaymentMethod> {
        return listOf(
            PaymentMethod("Visa", "1234", "12/25"),
            PaymentMethod("Mastercard", "5678", "08/26")
        )
    }
}
