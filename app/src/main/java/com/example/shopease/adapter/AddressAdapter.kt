package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.data.Address
import com.google.android.material.chip.Chip

class AddressAdapter(private val addresses: List<Address>) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.address_item, parent, false)
        return AddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = addresses[position]
        holder.bind(address)
    }

    override fun getItemCount(): Int {
        return addresses.size
    }

    inner class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fullName: TextView = itemView.findViewById(R.id.address_full_name)
        private val addressLine1: TextView = itemView.findViewById(R.id.address_line_1)
        private val cityPostalCode: TextView = itemView.findViewById(R.id.address_city_postal_code)
        private val country: TextView = itemView.findViewById(R.id.address_country)
        private val defaultChip: Chip = itemView.findViewById(R.id.default_address_chip)

        fun bind(address: Address) {
            fullName.text = address.fullName
            addressLine1.text = address.addressLine1
            cityPostalCode.text = "${address.city}, ${address.postalCode}"
            country.text = address.country
            defaultChip.visibility = if (address.isDefault) View.VISIBLE else View.GONE
        }
    }
}