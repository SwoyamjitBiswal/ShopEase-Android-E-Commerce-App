package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shopease.R

class LanguageSettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_language_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val languageRadioGroup: RadioGroup = view.findViewById(R.id.language_radio_group)

        languageRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val language = when (checkedId) {
                R.id.english_radio_button -> "English"
                R.id.spanish_radio_button -> "Spanish"
                R.id.french_radio_button -> "French"
                else -> ""
            }
            Toast.makeText(requireContext(), "Language changed to $language (Coming Soon!)", Toast.LENGTH_SHORT).show()
        }
    }
}
