package com.example.shopease.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.shopease.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class EditProfileFragment : Fragment() {

    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var profileImage: ImageView
    private var selectedImageUri: Uri? = null

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            profileImage.setImageURI(selectedImageUri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameEditText = view.findViewById(R.id.edit_profile_name)
        emailEditText = view.findViewById(R.id.edit_profile_email)
        profileImage = view.findViewById(R.id.edit_profile_image)
        val saveButton: Button = view.findViewById(R.id.save_profile_button)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            nameEditText.setText(user.displayName)
            emailEditText.setText(user.email)
            emailEditText.isEnabled = false // Don't allow email editing
            Glide.with(this).load(user.photoUrl).into(profileImage)
        }

        profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePickerLauncher.launch(intent)
        }

        saveButton.setOnClickListener {
            if (selectedImageUri != null) {
                uploadImageAndUpdateProfile()
            } else {
                updateProfile(null)
            }
        }
    }

    private fun uploadImageAndUpdateProfile() {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("profile_images/${user.uid}/${UUID.randomUUID()}")

        imageRef.putFile(selectedImageUri!!)
            .addOnSuccessListener { 
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    updateProfile(uri)
                }
            }
            .addOnFailureListener { 
                Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateProfile(imageUrl: Uri?) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val newName = nameEditText.text.toString()

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newName)
            .apply { if (imageUrl != null) setPhotoUri(imageUrl) }
            .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Profile update failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
