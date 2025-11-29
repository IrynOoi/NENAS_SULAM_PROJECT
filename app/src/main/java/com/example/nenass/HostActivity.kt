//HostActivity.kt
package com.example.nenass

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HostActivity : AppCompatActivity() {

    private var firebaseToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.host_activity)

        // Get Firebase token from LoginActivity
        firebaseToken = intent.getStringExtra("firebase_token")

        val navBottom = findViewById<BottomNavigationView>(R.id.navBottom)

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        // Handle bottom navigation
        navBottom.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_marketplace -> loadFragment(MarketFragment())
                R.id.nav_schedule -> loadFragment(
                    ScheduleFragment.newInstance(firebaseToken)
                )

                R.id.nav_connection -> loadFragment(ConnectionFragment())
                R.id.nav_profile -> loadFragment(ProfileFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
