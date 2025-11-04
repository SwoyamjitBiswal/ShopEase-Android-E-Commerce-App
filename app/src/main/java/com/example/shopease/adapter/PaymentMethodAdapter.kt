package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.data.PaymentMethod

class PaymentMethodAdapter(private val paymentMethods: List<PaymentMethod>) : RecyclerView.Adapter<PaymentMethodAdapter.PaymentMethodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.payment_method_item, parent, false)
        return PaymentMethodViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
        val paymentMethod = paymentMethods[position]
        holder.bind(paymentMethod)
    }

    override fun getItemCount(): Int {
        return paymentMethods.size
    }

    inner class PaymentMethodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardTypeIcon: ImageView = itemView.findViewById(R.id.card_type_icon)
        private val cardNumber: TextView = itemView.findViewById(R.id.card_number)
        private val cardExpiry: TextView = itemView.findViewById(R.id.card_expiry)

        fun bind(paymentMethod: PaymentMethod) {
            cardNumber.text = "${paymentMethod.cardType} ending in ${paymentMethod.lastFourDigits}"
            cardExpiry.text = "Expires ${paymentMethod.expiryDate}"
            // Here you could set the icon based on the card type
        }
    }
}