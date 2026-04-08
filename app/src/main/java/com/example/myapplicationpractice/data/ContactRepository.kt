package com.example.myapplicationpractice.data

import com.example.myapplicationpractice.models.Contact

object ContactRepository {

    fun getContacts(): List<Contact> = listOf(
        Contact(
            id = "1",
            name = "Sara Khan",
            relationship = "Spouse",
            phone = "+92 300 1234567",
            priority = 1,
            canAccessVault = true,
            notifyOnCheckIn = true,
            notifyOnSos = true
        ),
        Contact(
            id = "2",
            name = "Ahmed Khan",
            relationship = "Brother",
            phone = "+92 301 9988776",
            priority = 2,
            canAccessVault = false,
            notifyOnCheckIn = true,
            notifyOnSos = true
        ),
        Contact(
            id = "3",
            name = "Fatima Begum",
            relationship = "Mother",
            phone = "+92 302 3344556",
            priority = 3,
            canAccessVault = true,
            notifyOnCheckIn = false,
            notifyOnSos = true
        ),
        Contact(
            id = "4",
            name = "Usman Ali",
            relationship = "Lawyer",
            phone = "+92 333 7654321",
            priority = 4,
            canAccessVault = true,
            notifyOnCheckIn = false,
            notifyOnSos = true
        )
    )
}

