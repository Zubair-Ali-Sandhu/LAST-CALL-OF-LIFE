package com.example.myapplicationpractice.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.db.ContactDao
import com.example.myapplicationpractice.models.Contact
import com.example.myapplicationpractice.navigation.NavKeys
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragment for creating and editing emergency contacts (F3: CRUD).
 * Uses fragment_add_contact.xml layout.
 *
 * - If ARG_CONTACT is passed, pre-fills the form for editing (UPDATE).
 * - Otherwise, acts as a creation form (INSERT).
 * - All DB operations run on Dispatchers.IO ✓
 */
class AddEditContactFragment : Fragment(R.layout.fragment_add_contact) {

    private lateinit var dao: ContactDao
    private var editingContact: Contact? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dao = ContactDao(requireContext())

        val toolbar        = view.findViewById<MaterialToolbar>(R.id.toolbar)
        val etName         = view.findViewById<TextInputEditText>(R.id.et_name)
        val etPhone        = view.findViewById<TextInputEditText>(R.id.et_phone)
        val spRelationship = view.findViewById<AutoCompleteTextView>(R.id.sp_relationship)
        val switchVault    = view.findViewById<SwitchMaterial>(R.id.toggle_vault_access)
        val switchCheckin  = view.findViewById<SwitchMaterial>(R.id.toggle_notify_checkin)
        val switchSos      = view.findViewById<SwitchMaterial>(R.id.toggle_notify_sos)
        val btnSave        = view.findViewById<AppCompatButton>(R.id.btn_save)

        // Populate relationship dropdown
        val relationships = listOf(
            "Spouse", "Partner", "Parent", "Child", "Sibling",
            "Friend", "Colleague", "Lawyer", "Doctor", "Other"
        )
        val relAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            relationships
        )
        spRelationship.setAdapter(relAdapter)

        // Toolbar back navigation
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        // Check if editing an existing contact
        editingContact = arguments?.let { args ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                args.getParcelable(NavKeys.ARG_CONTACT, Contact::class.java)
            } else {
                @Suppress("DEPRECATION")
                args.getParcelable(NavKeys.ARG_CONTACT)
            }
        }

        // Pre-fill form if editing
        editingContact?.let { contact ->
            toolbar.title = "Edit Contact"
            etName.setText(contact.name)
            etPhone.setText(contact.phone)
            spRelationship.setText(contact.relationship, false)
            switchVault.isChecked = contact.canAccessVault
            switchCheckin.isChecked = contact.notifyOnCheckIn
            switchSos.isChecked = contact.notifyOnSos
            btnSave.text = "UPDATE CONTACT"
        }

        btnSave.setOnClickListener {
            val name         = etName.text?.toString()?.trim().orEmpty()
            val phone        = etPhone.text?.toString()?.trim().orEmpty()
            val relationship = spRelationship.text?.toString()?.trim().orEmpty()

            if (name.isBlank() || phone.isBlank()) {
                Toast.makeText(requireContext(), "Name and phone are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val contact = Contact(
                id             = editingContact?.id ?: 0,
                name           = name,
                relationship   = relationship,
                phone          = phone,
                priority       = editingContact?.priority ?: 1,
                canAccessVault = switchVault.isChecked,
                notifyOnCheckIn = switchCheckin.isChecked,
                notifyOnSos    = switchSos.isChecked,
                groupId        = editingContact?.groupId ?: 1
            )

            viewLifecycleOwner.lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    if (editingContact != null) {
                        dao.updateContact(contact)
                    } else {
                        dao.insertContact(contact)
                    }
                }
                Toast.makeText(
                    requireContext(),
                    if (editingContact != null) "Contact updated" else "Contact saved",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().popBackStack()
            }
        }
    }
}
