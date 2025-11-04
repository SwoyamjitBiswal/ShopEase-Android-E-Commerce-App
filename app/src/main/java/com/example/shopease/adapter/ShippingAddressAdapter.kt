package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.data.ShippingAddress

class ShippingAddressAdapter(
    private val onItemClick: (ShippingAddress) -> Unit
) : ListAdapter<ShippingAddress, ShippingAddressAdapter.ShippingAddressViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingAddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shipping_address_item, parent, false)
        return ShippingAddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShippingAddressViewHolder, position: Int) {
        val address = getItem(position)
        holder.bind(address)
        holder.itemView.setOnClickListener { onItemClick(address) }
    }

    class ShippingAddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fullName: TextView = itemView.findViewById(R.id.shipping_address_full_name)
        private val addressLine1: TextView = itemView.findViewById(R.id.shipping_address_line_1)
        private val cityStateZip: TextView = itemView.findViewById(R.id.shipping_address_city_state_zip)

        fun bind(address: ShippingAddress) {
            fullName.text = address.fullName
            addressLine1.text = address.addressLine1
            cityStateZip.text = "${address.city}, ${address.state} ${address.zipCode}"
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ShippingAddress>() {
        override fun areItemsTheSame(oldItem: ShippingAddress, newItem: ShippingAddress): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShippingAddress, newItem: ShippingAddress): Boolean {
            return oldItem == newItem
        }
    }
}
