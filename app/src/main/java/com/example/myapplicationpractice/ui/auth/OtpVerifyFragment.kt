package com.example.myapplicationpractice.ui.auth
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.activities.MainActivity
class OtpVerifyFragment : Fragment(R.layout.fragment_otp_verify) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.btn_verify)?.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        view.findViewById<View>(R.id.tv_resend)?.setOnClickListener {
            Toast.makeText(requireContext(), "OTP resent", Toast.LENGTH_SHORT).show()
        }
    }
}
