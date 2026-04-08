package com.example.myapplicationpractice.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplicationpractice.R

class HomeFragment : Fragment(R.layout.fragment_home) {

    companion object {
        private const val ARG_USER_NAME = "userName"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userName = arguments?.getString(ARG_USER_NAME).orEmpty()
        if (userName.isNotBlank()) {
            view.findViewById<TextView>(R.id.tv_user_name).text = userName
        }
    }
}

