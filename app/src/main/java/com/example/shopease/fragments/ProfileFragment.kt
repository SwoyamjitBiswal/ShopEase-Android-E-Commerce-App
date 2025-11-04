package com.example.shopease.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.shopease.R
import com.example.shopease.fragments.admin.AdminLoginFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var profileImage: CircleImageView
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var googleLoginButton: MaterialButton
    private lateinit var logoutButton: MaterialButton
    private lateinit var myOrdersButton: TextView
    private lateinit var shippingAddressesButton: TextView
    private lateinit var paymentMethodsButton: TextView
    private lateinit var notificationsButton: TextView
    private lateinit var languageButton: TextView
    private lateinit var editProfileButton: ImageButton
    private lateinit var adminLoginButton: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileImage = view.findViewById(R.id.profile_image)
        profileName = view.findViewById(R.id.profile_name)
        profileEmail = view.findViewById(R.id.profile_email)
        googleLoginButton = view.findViewById(R.id.google_login_button)
        logoutButton = view.findViewById(R.id.logout_button)
        myOrdersButton = view.findViewById(R.id.my_orders_button)
        shippingAddressesButton = view.findViewById(R.id.shipping_addresses_button)
        paymentMethodsButton = view.findViewById(R.id.payment_methods_button)
        notificationsButton = view.findViewById(R.id.notifications_button)
        languageButton = view.findViewById(R.id.language_button)
        editProfileButton = view.findViewById(R.id.edit_profile_button)
        adminLoginButton = view.findViewById(R.id.admin_login_button_profile)

        myOrdersButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MyOrdersFragment())
                .addToBackStack(null)
                .commit()
        }

        shippingAddressesButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ShippingAddressesFragment())
                .addToBackStack(null)
                .commit()
        }

        paymentMethodsButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PaymentMethodsFragment())
                .addToBackStack(null)
                .commit()
        }

        notificationsButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NotificationsSettingsFragment())
                .addToBackStack(null)
                .commit()
        }

        languageButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LanguageSettingsFragment())
                .addToBackStack(null)
                .commit()
        }

        editProfileButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EditProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        adminLoginButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AdminLoginFragment())
                .addToBackStack(null)
                .commit()
        }

        googleLoginButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .addToBackStack(null)
                .commit()
        }

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onResume() {
        super.onResume()
        val currentUser = firebaseAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: com.google.firebase.auth.FirebaseUser?) {
        if (user != null) {
            profileName.text = if (!user.displayName.isNullOrEmpty()) user.displayName else "ShopEase User"
            profileEmail.text = user.email ?: "No email provided"

            Glide.with(this)
                .load(user.photoUrl)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .circleCrop()
                .into(profileImage)

            googleLoginButton.visibility = View.GONE
            logoutButton.visibility = View.VISIBLE
            editProfileButton.visibility = View.VISIBLE
            logoutButton.setOnClickListener { signOut() }
        } else {
            profileName.text = getString(R.string.guest)
            profileEmail.text = getString(R.string.guest_email)
            profileImage.setImageResource(R.drawable.ic_person)

            googleLoginButton.visibility = View.VISIBLE
            logoutButton.visibility = View.GONE
            editProfileButton.visibility = View.GONE
        }
    }

    private fun signOut() {
        firebaseAuth.signOut()
        googleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
            updateUI(null)
        }
    }
}
