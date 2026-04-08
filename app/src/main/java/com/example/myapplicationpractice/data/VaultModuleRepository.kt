package com.example.myapplicationpractice.data

import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.models.VaultModule

object VaultModuleRepository {

    fun getModules(): List<VaultModule> = listOf(
        VaultModule(
            id = "medical",
            title = "Medical Profile",
            description = "Blood type, conditions, medications, allergies and organ donor status",
            iconRes = R.drawable.ic_medical,
            itemCount = 3,
            isLocked = false
        ),
        VaultModule(
            id = "assets",
            title = "Assets",
            description = "Properties, vehicles, jewelry, and digital assets",
            iconRes = R.drawable.ic_assets,
            itemCount = 5,
            isLocked = false
        ),
        VaultModule(
            id = "finances",
            title = "Financial Obligations",
            description = "Debts owed, amounts to collect, and insurance policies",
            iconRes = R.drawable.ic_finance,
            itemCount = 2,
            isLocked = false
        ),
        VaultModule(
            id = "estate",
            title = "Estate Planning",
            description = "Executor details, heirs, beneficiaries and asset assignments",
            iconRes = R.drawable.ic_estate,
            itemCount = 4,
            isLocked = false
        ),
        VaultModule(
            id = "threats",
            title = "Security & Threats",
            description = "Documented threats, risky individuals and dangerous situations",
            iconRes = R.drawable.ic_shield,
            itemCount = 1,
            isLocked = true
        ),
        VaultModule(
            id = "wishes",
            title = "Final Wishes",
            description = "Funeral preferences, last wishes and personal messages",
            iconRes = R.drawable.ic_wishes,
            itemCount = 0,
            isLocked = true
        )
    )
}
