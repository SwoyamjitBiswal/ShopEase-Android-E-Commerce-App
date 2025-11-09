package com.example.shopease.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var profileImage: CircleImageView
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var loginSignupButton: MaterialButton
    private lateinit var logoutButton: MaterialButton
    private lateinit var myOrdersButton: TextView
    private lateinit var shippingAddressesButton: TextView
    private lateinit var paymentMethodsButton: TextView
    private lateinit var notificationsButton: TextView
    private lateinit var languageButton: TextView
    private lateinit var editProfileButton: TextView
    private lateinit var adminLoginButton: TextView

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "Google Sign-In failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        setupClickListeners()
        setupFirebaseAuth()
    }

    override fun onStart() {
        super.onStart()
        updateUI(firebaseAuth.currentUser)
    }

    private fun setupViews(view: View) {
        profileImage = view.findViewById(R.id.profile_image)
        profileName = view.findViewById(R.id.profile_name)
        profileEmail = view.findViewById(R.id.profile_email)
        loginSignupButton = view.findViewById(R.id.login_signup_button)
        logoutButton = view.findViewById(R.id.logout_button)
        myOrdersButton = view.findViewById(R.id.my_orders_button)
        shippingAddressesButton = view.findViewById(R.id.shipping_addresses_button)
        paymentMethodsButton = view.findViewById(R.id.payment_methods_button)
        notificationsButton = view.findViewById(R.id.notifications_button)
        languageButton = view.findViewById(R.id.language_button)
        editProfileButton = view.findViewById(R.id.edit_profile_button)
        adminLoginButton = view.findViewById(R.id.admin_login_button_profile)
    }

    private fun setupClickListeners() {
        myOrdersButton.setOnClickListener { navigateTo(MyOrdersFragment()) }
        shippingAddressesButton.setOnClickListener { navigateTo(ShippingAddressesFragment()) }
        paymentMethodsButton.setOnClickListener { navigateTo(PaymentMethodsFragment()) }
        notificationsButton.setOnClickListener { navigateTo(NotificationsSettingsFragment()) }
        languageButton.setOnClickListener { navigateTo(LanguageSettingsFragment()) }
        editProfileButton.setOnClickListener { navigateTo(EditProfileFragment()) }
        adminLoginButton.setOnClickListener { navigateTo(AdminLoginFragment()) }
        loginSignupButton.setOnClickListener { navigateTo(LoginFragment()) }
        logoutButton.setOnClickListener { signOut() }
    }

    private fun setupFirebaseAuth() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.addAuthStateListener { auth ->
            updateUI(auth.currentUser)
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // AuthStateListener will handle UI update
                } else {
                    Toast.makeText(requireContext(), "Firebase Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signOut() {
        firebaseAuth.signOut()
        googleSignInClient.signOut()
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            profileName.text = user.displayName ?: "ShopEase User"
            profileEmail.text = user.email
            Glide.with(this).load(user.photoUrl).placeholder(R.drawable.ic_person).into(profileImage)
            loginSignupButton.visibility = View.GONE
            logoutButton.visibility = View.VISIBLE
            editProfileButton.visibility = View.VISIBLE
        } else {
            profileName.text = getString(R.string.guest)
            profileEmail.text = getString(R.string.guest_email)
            profileImage.setImageResource(R.drawable.ic_person)
            loginSignupButton.visibility = View.VISIBLE
            logoutButton.visibility = View.GONE
            editProfileButton.visibility = View.GONE
        }
    }

    private fun navigateTo(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
