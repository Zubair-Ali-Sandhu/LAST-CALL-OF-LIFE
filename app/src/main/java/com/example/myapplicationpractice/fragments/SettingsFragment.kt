package com.example.myapplicationpractice.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplicationpractice.R

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigate to Profile
        view.findViewById<View>(R.id.card_profile_preview)?.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_profile)
        }

        // Navigate to Security Settings
        view.findViewById<View>(R.id.pref_security)?.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_security)
        }

        // Navigate to Notification Settings
        view.findViewById<View>(R.id.pref_notifications)?.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_notifications)
        }
    }
}
