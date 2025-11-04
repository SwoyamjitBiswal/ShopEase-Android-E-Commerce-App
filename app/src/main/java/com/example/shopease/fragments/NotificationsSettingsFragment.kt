package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shopease.R
import com.google.android.material.switchmaterial.SwitchMaterial

class NotificationsSettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val promotionalNotificationsSwitch: SwitchMaterial = view.findViewById(R.id.promotional_notifications_switch)

        promotionalNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(requireContext(), "Promotional offers notifications ${if (isChecked) "enabled" else "disabled"}", Toast.LENGTH_SHORT).show()
        }
    }
}
