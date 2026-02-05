package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.data.PaymentMethod

class PaymentMethodAdapter(
    private val onDeleteClicked: (PaymentMethod) -> Unit
) : ListAdapter<PaymentMethod, PaymentMethodAdapter.PaymentMethodViewHolder>(
    object : DiffUtil.ItemCallback<PaymentMethod>() {
        override fun areItemsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.payment_method_item, parent, false)
        return PaymentMethodViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
        val paymentMethod = getItem(position)
        holder.bind(paymentMethod, onDeleteClicked)
    }

    class PaymentMethodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardIcon: ImageView = itemView.findViewById(R.id.card_icon)
        private val cardNumberTextView: TextView = itemView.findViewById(R.id.card_number)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)

        fun bind(paymentMethod: PaymentMethod, onDeleteClicked: (PaymentMethod) -> Unit) {
            // Correctly derive the last four digits from the full card number
            cardNumberTextView.text = "**** **** **** ${paymentMethod.cardNumber.takeLast(4)}"
            deleteButton.setOnClickListener { onDeleteClicked(paymentMethod) }

            when (paymentMethod.cardType) {
                "Visa" -> cardIcon.setImageResource(R.drawable.ic_visa)
                "MasterCard" -> cardIcon.setImageResource(R.drawable.ic_mastercard)
                else -> cardIcon.setImageResource(R.drawable.ic_credit_card)
            }
        }
    }
}
