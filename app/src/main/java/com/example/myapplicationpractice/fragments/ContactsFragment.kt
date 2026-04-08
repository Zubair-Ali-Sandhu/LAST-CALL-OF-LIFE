package com.example.myapplicationpractice.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.adapters.ContactAdapter
import com.example.myapplicationpractice.data.ContactRepository
import com.example.myapplicationpractice.models.Contact

class ContactsFragment : Fragment(R.layout.fragment_contacts) {

    private lateinit var adapter: ContactAdapter
    private var allContacts: List<Contact> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_contacts)
        val emptyState = view.findViewById<View>(R.id.layout_empty_state)
        val countText = view.findViewById<TextView>(R.id.tv_contact_count)
        val searchInput = view.findViewById<EditText>(R.id.et_search_contacts)

        adapter = ContactAdapter { contact ->
            findNavController().navigate(
                R.id.action_contacts_to_contactDetail,
                bundleOf("contact" to contact)
            )
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        allContacts = ContactRepository.getContacts()
        showData(allContacts, emptyState, countText)

        searchInput.doAfterTextChanged { text ->
            val query = text?.toString().orEmpty().trim()
            val filtered = if (query.isBlank()) {
                allContacts
            } else {
                allContacts.filter {
                    it.name.contains(query, ignoreCase = true) ||
                        it.relationship.contains(query, ignoreCase = true)
                }
            }
            showData(filtered, emptyState, countText)
        }
    }

    private fun showData(
        contacts: List<Contact>,
        emptyState: View,
        countText: TextView
    ) {
        adapter.submitList(contacts)
        emptyState.visibility = if (contacts.isEmpty()) View.VISIBLE else View.GONE
        countText.text = "${contacts.size}/5"
    }
}

