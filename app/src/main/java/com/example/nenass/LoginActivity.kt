//LoginActivity.kt
package com.example.nenass

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nenass.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Navigate to RegisterActivity
        binding.newAccBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Login button
        binding.signInBtn.setOnClickListener {
            val email = binding.emailInput2.text.toString().trim()
            val pass = binding.passwordInput.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT)
                    .show()
                Log.w("LOGIN_WARNING", "Attempted login with empty fields")
                return@setOnClickListener
            }

            // Sign in with Firebase Auth
            firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        Log.d("LOGIN", "Login successful for user: $email")
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                        // Get Firebase ID token
                        user?.getIdToken(true)?.addOnSuccessListener { result ->
                            val token = result.token
                            if (token != null) {
                                // Pass token to HostActivity
                                val intent = Intent(this, HostActivity::class.java)
                                intent.putExtra("firebase_token", token)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Failed to get Firebase token",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e("LOGIN_TOKEN", "Token is null")
                            }
                        }?.addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Failed to get Firebase token",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("LOGIN_TOKEN", "Failed to get token", e)
                        }

                    } else {
                        Toast.makeText(
                            this,
                            "Login failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("LOGIN_ERROR", "Login failed", task.exception)
                    }
                }
        }

        // Back button click listener
        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
