package com.example.myapplicationpractice.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.models.Contact
import com.example.myapplicationpractice.navigation.NavKeys

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

        bindStatusIcon(
            view = view,
            viewId = R.id.iv_checkin_notify_status,
            isEnabled = contact.notifyOnCheckIn
        )
        bindStatusIcon(
            view = view,
            viewId = R.id.iv_sos_notify_status,
            isEnabled = contact.notifyOnSos
        )
        bindStatusIcon(
            view = view,
            viewId = R.id.iv_vault_access_status,
            isEnabled = contact.canAccessVault
        )
    }

    private fun readContactFromArgs(): Contact? {
        val args = arguments ?: return null
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            args.getParcelable(NavKeys.ARG_CONTACT, Contact::class.java)
        } else {
            @Suppress("DEPRECATION")
            args.getParcelable(NavKeys.ARG_CONTACT)
        }
    }

    private fun bindStatusIcon(view: View, viewId: Int, isEnabled: Boolean) {
        val icon = view.findViewById<android.widget.ImageView>(viewId)
        val tintRes = if (isEnabled) R.color.accent_safe else R.color.text_tertiary
        icon.imageTintList = android.content.res.ColorStateList.valueOf(
            androidx.core.content.ContextCompat.getColor(requireContext(), tintRes)
        )
    }
}

