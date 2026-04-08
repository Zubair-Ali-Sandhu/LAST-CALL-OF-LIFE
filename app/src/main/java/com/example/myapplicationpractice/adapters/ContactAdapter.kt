package com.example.myapplicationpractice.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.models.Contact
import com.google.android.material.chip.Chip

class ContactAdapter(
    private val onItemClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private val items = mutableListOf<Contact>()

    fun submitList(data: List<Contact>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int = items.size

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameText: TextView = itemView.findViewById(R.id.tv_name)
        private val relationshipText: TextView = itemView.findViewById(R.id.tv_relationship)
        private val vaultBadge: ImageView = itemView.findViewById(R.id.iv_vault_badge)
        private val notifyBadge: ImageView = itemView.findViewById(R.id.iv_notify_badge)
        private val priorityChip: Chip = itemView.findViewById(R.id.chip_priority)

        fun bind(contact: Contact) {
            nameText.text = contact.name
            relationshipText.text = contact.relationship
            priorityChip.text = "#${contact.priority}"
            vaultBadge.visibility = if (contact.canAccessVault) View.VISIBLE else View.GONE
            notifyBadge.visibility = if (contact.notifyOnSos || contact.notifyOnCheckIn) View.VISIBLE else View.GONE
        }
    }
}

