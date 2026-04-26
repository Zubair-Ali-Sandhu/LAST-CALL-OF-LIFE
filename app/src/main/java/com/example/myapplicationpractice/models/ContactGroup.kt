package com.example.myapplicationpractice.models

/**
 * Represents a group/category for emergency contacts.
 * Maps to the 'contact_groups' SQLite table (F2).
 */
data class ContactGroup(
    val id: Long = 0,
    val groupName: String,
    val description: String,
    val createdAt: String = ""
)
