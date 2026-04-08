package com.example.myapplicationpractice.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.models.Contact

class ContactDetailFragment : Fragment(R.layout.fragment_contact_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contact = readContactFromArgs() ?: return

        view.findViewById<TextView>(R.id.tv_name).text = contact.name
        view.findViewById<TextView>(R.id.tv_phone).text = contact.phone
        view.findViewById<TextView>(R.id.tv_relationship).text = contact.relationship

        val chip = view.findViewById<com.google.android.material.chip.Chip>(R.id.chip_vault_access)
        chip.text = if (contact.canAccessVault) {
            "Vault Access Granted"
        } else {
            "No Vault Access"
        }
    }

    private fun readContactFromArgs(): Contact? {
        val args = arguments ?: return null
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            args.getParcelable("contact", Contact::class.java)
        } else {
            @Suppress("DEPRECATION")
            args.getParcelable("contact")
        }
    }
}

