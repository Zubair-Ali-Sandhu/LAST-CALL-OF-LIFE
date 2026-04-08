package com.example.myapplicationpractice.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val id: String,
    val name: String,
    val relationship: String,
    val phone: String,
    val priority: Int,
    val canAccessVault: Boolean,
    val notifyOnCheckIn: Boolean,
    val notifyOnSos: Boolean
) : Parcelable

