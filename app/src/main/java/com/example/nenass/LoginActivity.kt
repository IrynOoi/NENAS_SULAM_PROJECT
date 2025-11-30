//LoginActivity.kt

package com.example.nenass

import android.util.Log
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nenass.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        // ======= DEV MODE START =======
//        val isDevMode = true
//        if (isDevMode) {
//            val intent = Intent(this, HostActivity::class.java)
//            startActivity(intent)
//            finish() // close LoginActivity
//            return // skip the rest of onCreate
//        }
//        // ======= DEV MODE END =======
        // Inflate binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Navigate to RegisterActivity
        binding.newAccBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Login button
        // Inside onCreate()
        binding.signInBtn.setOnClickListener {
            val email = binding.emailInput2.text.toString().trim()
            val pass = binding.passwordInput.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            Log.d("LOGIN", "Login successful for user: $email")
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                            // TODO: Navigate to next activity

                            val intent = Intent(this, HostActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {

                            // Print full detailed error in Logcat
                            Log.e("LOGIN_ERROR", "Login failed", task.exception)

                            // Show readable message to user
                            Toast.makeText(
                                this,
                                "Login failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            } else {
                Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT)
                    .show()

                // Log for debugging
                Log.w("LOGIN_WARNING", "Attempted login with empty fields")
            }
        }

        // Back button click listener
        binding.backBtn.setOnClickListener {
            // Navigate back to CoverPageActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // optional, closes LoginActivity so user can't go back here
        }

    }
}