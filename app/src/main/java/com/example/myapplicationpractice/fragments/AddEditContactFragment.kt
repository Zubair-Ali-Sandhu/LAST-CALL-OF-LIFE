package com.example.myapplicationpractice.fragments

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.db.ContactDao
import com.example.myapplicationpractice.models.Contact
import com.example.myapplicationpractice.models.ContactGroup
import com.example.myapplicationpractice.navigation.NavKeys
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragment for creating and editing emergency contacts (F3: CRUD).
 *
 * - If ARG_CONTACT is passed, pre-fills the form for editing (UPDATE).
 * - Otherwise, acts as a creation form (INSERT).
 * - All DB operations run on Dispatchers.IO ✓
 */
class AddEditContactFragment : Fragment(R.layout.fragment_add_edit_contact) {

    private lateinit var dao: ContactDao
    private var groups: List<ContactGroup> = emptyList()
    private var editingContact: Contact? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dao = ContactDao(requireContext())

        val etName = view.findViewById<TextInputEditText>(R.id.et_name)
        val etRelationship = view.findViewById<TextInputEditText>(R.id.et_relationship)
        val etPhone = view.findViewById<TextInputEditText>(R.id.et_phone)
        val etPriority = view.findViewById<TextInputEditText>(R.id.et_priority)
        val spinnerGroup = view.findViewById<Spinner>(R.id.spinner_group)
        val switchVault = view.findViewById<SwitchMaterial>(R.id.switch_vault_access)
        val switchCheckin = view.findViewById<SwitchMaterial>(R.id.switch_notify_checkin)
        val switchSos = view.findViewById<SwitchMaterial>(R.id.switch_notify_sos)
        val btnSave = view.findViewById<MaterialButton>(R.id.btn_save_contact)

        // Check if editing an existing contact
        editingContact = arguments?.let { args ->
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                args.getParcelable(NavKeys.ARG_CONTACT, Contact::class.java)
            } else {
                @Suppress("DEPRECATION")
                args.getParcelable(NavKeys.ARG_CONTACT)
            }
        }

        // Load groups from DB for the spinner
        viewLifecycleOwner.lifecycleScope.launch {
            groups = withContext(Dispatchers.IO) { dao.getAllGroups() }

            val groupNames = groups.map { it.groupName }
            val spinnerAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                groupNames
            )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGroup.adapter = spinnerAdapter

            // Pre-fill form if editing
            editingContact?.let { contact ->
                // Switch toolbar title to "Edit Contact"
                view.findViewById<MaterialToolbar>(R.id.toolbar)?.title = "Edit Contact"

                etName.setText(contact.name)
                etRelationship.setText(contact.relationship)
                etPhone.setText(contact.phone)
                etPriority.setText(contact.priority.toString())
                switchVault.isChecked = contact.canAccessVault
                switchCheckin.isChecked = contact.notifyOnCheckIn
                switchSos.isChecked = contact.notifyOnSos
                btnSave.text = "UPDATE CONTACT"

                // Select the correct group in spinner
                val groupIndex = groups.indexOfFirst { it.id == contact.groupId }
                if (groupIndex >= 0) spinnerGroup.setSelection(groupIndex)
            }
        }

        btnSave.setOnClickListener {
            val name = etName.text?.toString()?.trim().orEmpty()
            val relationship = etRelationship.text?.toString()?.trim().orEmpty()
            val phone = etPhone.text?.toString()?.trim().orEmpty()
            val priorityStr = etPriority.text?.toString()?.trim().orEmpty()

            // Validation
            if (name.isBlank() || phone.isBlank()) {
                Toast.makeText(requireContext(), "Name and phone are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val priority = priorityStr.toIntOrNull() ?: 1
            val selectedGroupIndex = spinnerGroup.selectedItemPosition
            val groupId = if (selectedGroupIndex >= 0 && selectedGroupIndex < groups.size) {
                groups[selectedGroupIndex].id
            } else 1L

            val contact = Contact(
                id = editingContact?.id ?: 0,
                name = name,
                relationship = relationship,
                phone = phone,
                priority = priority,
                canAccessVault = switchVault.isChecked,
                notifyOnCheckIn = switchCheckin.isChecked,
                notifyOnSos = switchSos.isChecked,
                groupId = groupId
            )

            // Save on background thread
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
