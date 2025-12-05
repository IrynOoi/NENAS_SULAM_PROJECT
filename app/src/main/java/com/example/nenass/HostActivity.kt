//HostActivity.kt
package com.example.nenass

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.host_activity) // ensure host_activity.xml exists

        val navBottom = findViewById<BottomNavigationView>(R.id.navBottom)

        // Load default fragment (e.g., HomeFragment)
        if (savedInstanceState == null) {
            loadFragment(MarketFragment())
        }

        // Handle bottom navigation item clicks
        navBottom.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(MarketFragment())
//                R.id.nav_marketplace -> loadFragment(MarketFragment())
                R.id.nav_schedule -> loadFragment(ScheduleFragment())
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