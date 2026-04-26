package com.example.myapplicationpractice.data

import com.example.myapplicationpractice.models.Contact

/**
 * Legacy hardcoded contact repository — kept as a fallback / seed reference.
 * The app now uses SQLite via ContactDao for persistent storage (F3).
 */
object ContactRepository {

    fun getContacts(): List<Contact> = listOf(
        Contact(
            id = 1L,
            name = "Sara Khan",
            relationship = "Spouse",
            phone = "+92 300 1234567",
            priority = 1,
            canAccessVault = true,
            notifyOnCheckIn = true,
            notifyOnSos = true,
            groupId = 1L
        ),
        Contact(
            id = 2L,
            name = "Ahmed Khan",
            relationship = "Brother",
            phone = "+92 301 9988776",
            priority = 2,
            canAccessVault = false,
            notifyOnCheckIn = true,
            notifyOnSos = true,
            groupId = 1L
        ),
        Contact(
            id = 3L,
            name = "Fatima Begum",
            relationship = "Mother",
            phone = "+92 302 3344556",
            priority = 3,
            canAccessVault = true,
            notifyOnCheckIn = false,
            notifyOnSos = true,
            groupId = 1L
        ),
        Contact(
            id = 4L,
            name = "Usman Ali",
            relationship = "Lawyer",
            phone = "+92 333 7654321",
            priority = 4,
            canAccessVault = true,
            notifyOnCheckIn = false,
            notifyOnSos = true,
            groupId = 2L
        )
    )
}
