package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.shopease.R
import com.example.shopease.ShopEaseApplication
import com.example.shopease.viewmodels.PaymentViewModel
import com.example.shopease.viewmodels.ViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddPaymentMethodFragment : Fragment() {

    private lateinit var cardholderNameEditText: TextInputEditText
    private lateinit var cardNumberEditText: TextInputEditText
    private lateinit var expiryDateEditText: TextInputEditText
    private lateinit var cvvEditText: TextInputEditText

    private val viewModel: PaymentViewModel by viewModels {
        ViewModelFactory(requireActivity().application as ShopEaseApplication)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_payment_method, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }

        cardholderNameEditText = view.findViewById(R.id.cardholder_name_edit_text)
        cardNumberEditText = view.findViewById(R.id.card_number_edit_text)
        expiryDateEditText = view.findViewById(R.id.expiry_date_edit_text)
        cvvEditText = view.findViewById(R.id.cvv_edit_text)
        val saveCardButton: MaterialButton = view.findViewById(R.id.save_card_button)

        saveCardButton.setOnClickListener {
            val cardholderName = cardholderNameEditText.text.toString()
            val cardNumber = cardNumberEditText.text.toString()
            val expiryDate = expiryDateEditText.text.toString()
            val cvv = cvvEditText.text.toString()

            if (cardholderName.isNotEmpty() && cardNumber.isNotEmpty() && expiryDate.isNotEmpty() && cvv.isNotEmpty()) {
                // For simplicity, we'll assume the card type and get the last four digits
                val cardType = if (cardNumber.startsWith("4")) "Visa" else if (cardNumber.startsWith("5")) "MasterCard" else "Card"
                val lastFour = cardNumber.takeLast(4)

                viewModel.insertPaymentMethod(cardType, lastFour, cardholderName, expiryDate)
                
                Toast.makeText(requireContext(), "Card saved successfully!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
