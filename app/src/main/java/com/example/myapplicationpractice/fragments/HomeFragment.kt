package com.example.myapplicationpractice.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.navigation.NavKeys

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userName = arguments?.getString(NavKeys.ARG_USER_NAME).orEmpty()
        if (userName.isNotBlank()) {
            view.findViewById<TextView>(R.id.tv_user_name).text = userName
        }
    }
}

