package com.example.shopease

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.shopease.data.Product
import com.example.shopease.databinding.ActivityAddProductBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding
    private var imageUri: Uri? = null

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
        binding.ivProductImage.setImageURI(imageUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.flImagePicker.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.btnUploadProduct.setOnClickListener {
            uploadProduct()
        }
    }

    private fun uploadProduct() {
        val name = binding.etProductName.text.toString().trim()
        val description = binding.etProductDescription.text.toString().trim()
        val price = binding.etProductPrice.text.toString().trim()
        val category = binding.etProductCategory.text.toString().trim()

        if (name.isEmpty() || description.isEmpty() || price.isEmpty() || category.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.btnUploadProduct.isEnabled = false

        val storageRef = FirebaseStorage.getInstance().reference.child("products/${UUID.randomUUID()}")
        storageRef.putFile(imageUri!!)
            .addOnSuccessListener { 
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val product = Product(
                        id = UUID.randomUUID().toString(),
                        name = name,
                        description = description,
                        price = price.toDouble(),
                        category = category,
                        imageUrl = uri.toString(),
                        stock = 100,
                        rating = 0.0f
                    )

                    FirebaseDatabase.getInstance().getReference("products").child(product.id).setValue(product)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Product uploaded successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { 
                            Toast.makeText(this, "Failed to upload product details", Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                            binding.btnUploadProduct.isEnabled = true
                        }
                }
            }
            .addOnFailureListener { 
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                binding.btnUploadProduct.isEnabled = true
            }
    }
}
