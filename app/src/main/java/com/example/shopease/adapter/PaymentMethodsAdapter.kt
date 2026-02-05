package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.data.PaymentMethod

class PaymentMethodsAdapter(
    private val onDeleteClicked: (PaymentMethod) -> Unit
) : ListAdapter<PaymentMethod, PaymentMethodsAdapter.PaymentMethodViewHolder>(PaymentMethodDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.payment_method_item, parent, false)
        return PaymentMethodViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
        val paymentMethod = getItem(position)
        holder.bind(paymentMethod, onDeleteClicked)
    }

    class PaymentMethodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardNumberTextView: TextView = itemView.findViewById(R.id.card_number)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)

        fun bind(paymentMethod: PaymentMethod, onDeleteClicked: (PaymentMethod) -> Unit) {
            // Correctly derive the last four digits from the full card number
            cardNumberTextView.text = "**** **** **** ${paymentMethod.cardNumber.takeLast(4)}"
            deleteButton.setOnClickListener { onDeleteClicked(paymentMethod) }
        }
    }
}

class PaymentMethodDiffCallback : DiffUtil.ItemCallback<PaymentMethod>() {
    override fun areItemsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean {
        return oldItem == newItem
    }
}
