package com.example.myapplicationpractice.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Emergency contact data model.
 * Maps to the 'emergency_contacts' SQLite table (F2).
 * Changed id from String to Long for AUTOINCREMENT PK,
 * added groupId for FK relationship to contact_groups.
 */
@Parcelize
data class Contact(
    val id: Long = 0,
    val name: String,
    val relationship: String,
    val phone: String,
    val priority: Int,
    val canAccessVault: Boolean,
    val notifyOnCheckIn: Boolean,
    val notifyOnSos: Boolean,
    val groupId: Long = 1,
    val groupName: String = ""
) : Parcelable
