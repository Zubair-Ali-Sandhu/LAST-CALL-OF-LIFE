package com.example.myapplicationpractice.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.models.VaultModule
import com.example.myapplicationpractice.navigation.NavKeys

class VaultModuleDetailFragment : Fragment(R.layout.fragment_vault_module_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val module = readModuleFromArgs() ?: return

        view.findViewById<android.widget.ImageView>(R.id.iv_module_icon).setImageResource(module.iconRes)
        view.findViewById<android.widget.TextView>(R.id.tv_module_name).text = module.title
        view.findViewById<android.widget.TextView>(R.id.tv_module_description).text = module.description
        view.findViewById<android.widget.TextView>(R.id.tv_module_item_count).text =
            if (module.isLocked) "Locked module" else "${module.itemCount} items available"
    }

    private fun readModuleFromArgs(): VaultModule? {
        val args = arguments ?: return null
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            args.getParcelable(NavKeys.ARG_VAULT_MODULE, VaultModule::class.java)
        } else {
            @Suppress("DEPRECATION")
            args.getParcelable(NavKeys.ARG_VAULT_MODULE)
        }
    }
}

