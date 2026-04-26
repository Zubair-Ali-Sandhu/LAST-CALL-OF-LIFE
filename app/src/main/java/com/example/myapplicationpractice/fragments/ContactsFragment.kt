package com.example.myapplicationpractice.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.adapters.ContactAdapter
import com.example.myapplicationpractice.db.ContactDao
import com.example.myapplicationpractice.models.Contact
import com.example.myapplicationpractice.navigation.NavKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Contacts list fragment — now backed by SQLite (F3) with
 * dynamic search via LIKE and sort via ORDER BY (F5).
 *
 * All DB operations run on Dispatchers.IO ✓
 */
class ContactsFragment : Fragment(R.layout.fragment_contacts) {

    private lateinit var adapter: ContactAdapter
    private lateinit var dao: ContactDao
    private var currentSort = "priority ASC"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dao = ContactDao(requireContext())

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_contacts)
        val emptyState = view.findViewById<View>(R.id.layout_empty_state)
        val countText = view.findViewById<TextView>(R.id.tv_contact_count)
        val searchInput = view.findViewById<EditText>(R.id.et_search_contacts)
        val toolbar = view.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)

        adapter = ContactAdapter { contact ->
            findNavController().navigate(
                R.id.action_contacts_to_contactDetail,
                bundleOf(NavKeys.ARG_CONTACT to contact)
            )
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Sort menu on toolbar (F5: ORDER BY)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sort_name -> {
                    currentSort = "name ASC"
                    loadContacts(searchInput.text?.toString().orEmpty(), emptyState, countText)
                    true
                }
                R.id.sort_priority -> {
                    currentSort = "priority ASC"
                    loadContacts(searchInput.text?.toString().orEmpty(), emptyState, countText)
                    true
                }
                R.id.sort_date -> {
                    currentSort = "c.created_at DESC"
                    loadContacts(searchInput.text?.toString().orEmpty(), emptyState, countText)
                    true
                }
                else -> false
            }
        }

        // Search with SQL LIKE (F5)
        searchInput.doAfterTextChanged { text ->
            loadContacts(text?.toString().orEmpty().trim(), emptyState, countText)
        }

        // FAB to add a new contact
        view.findViewById<View>(R.id.btn_add_contact).setOnClickListener {
            findNavController().navigate(R.id.action_contacts_to_addEditContact)
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload contacts when returning from add/edit/delete
        val emptyState = view?.findViewById<View>(R.id.layout_empty_state) ?: return
        val countText = view?.findViewById<TextView>(R.id.tv_contact_count) ?: return
        val searchInput = view?.findViewById<EditText>(R.id.et_search_contacts) ?: return
        loadContacts(searchInput.text?.toString().orEmpty().trim(), emptyState, countText)
    }

    /**
     * Load contacts from SQLite on a background thread.
     * If query is non-blank, uses LIKE search (F5).
     * Uses current sort order via ORDER BY (F5).
     */
    private fun loadContacts(query: String, emptyState: View, countText: TextView) {
        viewLifecycleOwner.lifecycleScope.launch {
            val contacts: List<Contact> = withContext(Dispatchers.IO) {
                if (query.isBlank()) {
                    dao.getContactsSorted(currentSort)
                } else {
                    dao.searchContacts(query)
                }
            }
            showData(contacts, emptyState, countText)
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
