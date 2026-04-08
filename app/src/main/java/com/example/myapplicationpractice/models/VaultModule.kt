package com.example.myapplicationpractice.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VaultModule(
    val id: String,
    val title: String,
    val description: String,
    val iconRes: Int,
    val itemCount: Int,
    val isLocked: Boolean
) : Parcelable
