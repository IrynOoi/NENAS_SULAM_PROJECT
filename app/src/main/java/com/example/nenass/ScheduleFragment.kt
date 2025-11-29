//ScheduleFragment.kt
// ScheduleFragment.kt
package com.example.nenass

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class ScheduleFragment : Fragment() {

    private var firebaseUID: String? = null
    private var tokenInjected = false // ensure token injected only once

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get UID from arguments
        firebaseUID = arguments?.getString(ARG_UID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        WebView.setWebContentsDebuggingEnabled(true)

        val webView = WebView(requireContext())
        webView.settings.javaScriptEnabled = true

        // Capture JS console logs
        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                Log.d("WebView", consoleMessage?.message() ?: "")
                return true
            }
        }

        // Handle page load
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                if (tokenInjected) return // avoid duplicate injection

                Log.d("ScheduleFragment", "Injecting Firebase token into WebView")

                // Inject UID for reference
                val uid = firebaseUID ?: ""
                view?.evaluateJavascript(
                    "window.firebaseUID = '$uid';" +
                            "document.dispatchEvent(new Event('firebase-uid-ready'));",
                    null
                )

                // Inject Firebase ID token
                FirebaseAuth.getInstance().currentUser?.getIdToken(true)
                    ?.addOnSuccessListener { result ->
                        val token = result.token
                        if (token != null) {
                            view?.evaluateJavascript(
                                "window.firebaseToken = '$token';" +
                                        "document.dispatchEvent(new Event('firebase-token-ready'));",
                                null
                            )
                            tokenInjected = true
                            Log.d("ScheduleFragment", "Firebase token injected successfully")
                        } else {
                            Log.w("ScheduleFragment", "Firebase token is null")
                        }
                    }
                    ?.addOnFailureListener { e ->
                        Log.e("ScheduleFragment", "Failed to get Firebase token", e)
                    }
            }
        }

        // Load the React app from assets
        webView.loadUrl("file:///android_asset/build/index.html")

        return webView
    }

    companion object {
        private const val ARG_UID = "firebase_uid"

        fun newInstance(uid: String?): ScheduleFragment {
            val fragment = ScheduleFragment()
            val args = Bundle()
            args.putString(ARG_UID, uid)
            fragment.arguments = args
            return fragment
        }
    }
}
