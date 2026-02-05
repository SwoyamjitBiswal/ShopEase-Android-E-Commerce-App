package com.example.shopease.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.PaymentMethod
import com.example.shopease.data.ShoppingRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class PaymentViewModel(private val repository: ShoppingRepository) : ViewModel() {

    // DEBUGGING: Temporarily providing an empty list with explicit type
    private val _paymentMethods = MutableStateFlow<List<PaymentMethod>>(emptyList())
    val paymentMethods: StateFlow<List<PaymentMethod>> = _paymentMethods.asStateFlow()

    fun insertPaymentMethod(cardType: String, cardNumber: String, cardHolderName: String, expiryDate: String) {
        viewModelScope.launch {
            // DEBUGGING: Temporarily disabled
            /*
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val newPaymentMethod = PaymentMethod(
                id = UUID.randomUUID().toString(),
                userId = userId,
                cardType = cardType,
                cardNumber = cardNumber,
                cardHolderName = cardHolderName,
                expiryDate = expiryDate
            )
            repository.insertPaymentMethod(newPaymentMethod)
            */
        }
    }

    fun deletePaymentMethod(paymentMethod: PaymentMethod) {
        viewModelScope.launch {
            // DEBUGGING: Temporarily disabled
            // repository.deletePaymentMethod(paymentMethod)
        }
    }
}
