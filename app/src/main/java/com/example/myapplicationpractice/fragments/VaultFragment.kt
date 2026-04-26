package com.example.myapplicationpractice.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.adapters.VaultModuleAdapter
import com.example.myapplicationpractice.data.VaultModuleRepository
import com.example.myapplicationpractice.navigation.NavKeys

class VaultFragment : Fragment(R.layout.fragment_vault_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modules = VaultModuleRepository.getModules()
        view.findViewById<android.widget.TextView>(R.id.tv_modules_count).text =
            "${modules.size} modules configured"

        val recycler = view.findViewById<RecyclerView>(R.id.recycler_vault_modules)
        val adapter = VaultModuleAdapter { module ->
            findNavController().navigate(
                R.id.action_vaultHome_to_vaultModuleDetail,
                bundleOf(NavKeys.ARG_VAULT_MODULE to module)
            )
        }

        recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        recycler.adapter = adapter
        recycler.setHasFixedSize(true)
        adapter.submitList(modules)

        // Wire Unlock Vault button → navigate to OTP auth screen
        view.findViewById<View>(R.id.btn_unlock_vault).setOnClickListener {
            findNavController().navigate(R.id.action_vaultHome_to_vaultAuth)
        }

        // Wire "I Received an Emergency OTP" button
        view.findViewById<View>(R.id.btn_contact_vault_access).setOnClickListener {
            findNavController().navigate(R.id.action_vaultHome_to_vaultAuth)
        }
    }
}
