package com.example.myapplicationpractice.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplicationpractice.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val phoneInput = findViewById<EditText>(R.id.et_phone)
        val passwordInput = findViewById<EditText>(R.id.et_password)

        findViewById<android.view.View>(R.id.btn_login).setOnClickListener {
            val phone = phoneInput.text?.toString().orEmpty().trim()
            val password = passwordInput.text?.toString().orEmpty().trim()
            if (phone.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Enter phone and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userName = "User ${phone.takeLast(4)}"
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(MainActivity.EXTRA_USER_NAME, userName)
            }
            startActivity(intent)
            finish()
        }

        findViewById<TextView>(R.id.tv_register_link).setOnClickListener {
            Toast.makeText(this, "Register screen can be connected next", Toast.LENGTH_SHORT).show()
        }
    }
}

