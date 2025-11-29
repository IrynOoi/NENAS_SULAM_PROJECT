//ScheduleFragment.kt

package com.example.nenass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment

class ScheduleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 建立 WebView
        val webView = WebView(requireContext())

        // 啟用 JavaScript
        webView.settings.javaScriptEnabled = true

        // 避免用外部瀏覽器開啟
        webView.webViewClient = WebViewClient()

        // 載入 React build 的 index.html
        webView.loadUrl("file:///android_asset/build/index.html")

        return webView
    }
}