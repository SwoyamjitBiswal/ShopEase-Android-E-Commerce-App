package com.example.shopease

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.shopease.fragments.CartFragment
import com.example.shopease.fragments.HomeFragment
import com.example.shopease.fragments.ProfileFragment
import com.example.shopease.fragments.SearchFragment
import com.example.shopease.fragments.WishlistFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.navigation_home -> selectedFragment = HomeFragment()
                R.id.navigation_search -> selectedFragment = SearchFragment()
                R.id.navigation_cart -> selectedFragment = CartFragment()
                R.id.navigation_wishlist -> selectedFragment = WishlistFragment()
                R.id.navigation_profile -> selectedFragment = ProfileFragment()
            }
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
            }
            true
        }

        // This is the definitive fix for the startup crash.
        // We are now programmatically setting the initial fragment,
        // which is a more robust and stable pattern.
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.navigation_home
        }
    }
}
