package com.example.myapplicationpractice.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.db.ContactDao
import com.example.myapplicationpractice.models.Contact
import com.example.myapplicationpractice.navigation.NavKeys
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Contact detail fragment — now includes Edit and Delete functionality (F3: CRUD).
 * Delete runs on Dispatchers.IO ✓
 */
class ContactDetailFragment : Fragment(R.layout.fragment_contact_detail) {

    private lateinit var dao: ContactDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dao = ContactDao(requireContext())

        val contact = readContactFromArgs() ?: return

        // Back navigation + contact name in toolbar
        view.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)?.let { tb ->
            tb.setNavigationOnClickListener { findNavController().navigateUp() }
            tb.title = contact.name
        }

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

        // ── Edit button → navigate to AddEditContactFragment with contact data ──
        view.findViewById<View>(R.id.btn_edit).setOnClickListener {
            findNavController().navigate(
                R.id.action_contactDetail_to_addEditContact,
                bundleOf(NavKeys.ARG_CONTACT to contact)
            )
        }

        // ── Delete button → show confirmation dialog, then delete on background thread ──
        view.findViewById<View>(R.id.btn_delete).setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Remove Contact")
                .setMessage("Are you sure you want to remove ${contact.name} from your emergency contacts?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Remove") { _, _ ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            dao.deleteContact(contact.id)
                        }
                        Toast.makeText(requireContext(), "${contact.name} removed", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                }
                .show()
        }
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
