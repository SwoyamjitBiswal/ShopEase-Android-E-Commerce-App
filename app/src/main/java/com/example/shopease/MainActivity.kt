package com.example.shopease

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.shopease.fragments.CartFragment
import com.example.shopease.fragments.CategoriesFragment
import com.example.shopease.fragments.HomeFragment
import com.example.shopease.fragments.ProfileFragment
import com.example.shopease.viewmodel.CartViewModel
import com.example.shopease.viewmodels.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val cartViewModel: CartViewModel by viewModels {
        val application = application as ShopEaseApplication
        ViewModelFactory(application.container.shoppingRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_home -> selectedFragment = HomeFragment()
                R.id.nav_categories -> selectedFragment = CategoriesFragment()
                R.id.nav_cart -> selectedFragment = CartFragment()
                R.id.nav_profile -> selectedFragment = ProfileFragment()
            }
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
            }
            true
        }

        // Set the default fragment
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.nav_home
        }

        // Observe cart item count and update badge
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.allCartItems.collect { cartItems ->
                    val badge = bottomNavigationView.getOrCreateBadge(R.id.nav_cart)
                    if (cartItems.isEmpty()) {
                        badge.isVisible = false
                    } else {
                        badge.isVisible = true
                        // Correctly access the nested quantity property
                        badge.number = cartItems.sumOf { it.cartItem.quantity }
                    }
                }
            }
        }
    }
}
