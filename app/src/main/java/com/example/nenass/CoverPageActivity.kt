//CoverPageActivity
package com.example.nenass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.widget.Button
import com.example.nenass.R

class CoverPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cover_page)

        // 2秒後自動跳轉到LoginActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // 結束CoverPageActivity，按返回鍵不會回到閃屏
        }, 1000) // 2000ms = 2秒
    }
}