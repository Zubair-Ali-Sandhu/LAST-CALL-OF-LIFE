package com.example.myapplicationpractice.fragments
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplicationpractice.R
import com.google.android.material.appbar.MaterialToolbar
class VaultAuthFragment : Fragment(R.layout.fragment_vault_auth) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val otpBoxes = listOf<EditText>(
            view.findViewById(R.id.otp_1), view.findViewById(R.id.otp_2),
            view.findViewById(R.id.otp_3), view.findViewById(R.id.otp_4),
            view.findViewById(R.id.otp_5), view.findViewById(R.id.otp_6)
        )
        // Auto-advance between OTP boxes
        otpBoxes.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && index < otpBoxes.lastIndex) {
                        otpBoxes[index + 1].requestFocus()
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
        view.findViewById<View>(R.id.btn_verify_otp).setOnClickListener {
            // Accept any 6-digit entry as valid for demo
            val otp = otpBoxes.joinToString("") { it.text.toString() }
            if (otp.length == 6) {
                findNavController().navigate(R.id.action_vaultAuth_to_vaultMedical)
            }
        }
    }
}
