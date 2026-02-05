package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopease.R
import com.example.shopease.data.Product
import com.example.shopease.fragments.admin.EditProductFragment
import com.example.shopease.viewmodels.AdminViewModel

// Renamed to avoid redeclaration conflict with ProductAdapter
private class ProductManagementDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}

class ProductManagementAdapter(
    private val viewModel: AdminViewModel
) : ListAdapter<Product, ProductManagementAdapter.ProductViewHolder>(ProductManagementDiffCallback()) { // Use the renamed class

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_management_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productImage: ImageView = itemView.findViewById(R.id.product_image)
        private val productName: TextView = itemView.findViewById(R.id.product_name)
        private val productPrice: TextView = itemView.findViewById(R.id.product_price)
        private val editButton: ImageButton = itemView.findViewById(R.id.edit_product_button)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.delete_product_button)

        fun bind(product: Product) {
            productName.text = product.name
            productPrice.text = "$${product.price}"
            Glide.with(itemView.context).load(product.imageUrl).into(productImage)

            editButton.setOnClickListener { 
                val activity = it.context as AppCompatActivity
                val fragment = EditProductFragment.newInstance(product)
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            deleteButton.setOnClickListener {
                viewModel.deleteProduct(product)
                Toast.makeText(itemView.context, "Product deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
