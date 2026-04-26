package com.example.myapplicationpractice.fragments
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplicationpractice.R
import com.google.android.material.appbar.MaterialToolbar
class VaultWishesFragment : Fragment(R.layout.fragment_vault_wishes) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<MaterialToolbar>(R.id.toolbar)?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}
