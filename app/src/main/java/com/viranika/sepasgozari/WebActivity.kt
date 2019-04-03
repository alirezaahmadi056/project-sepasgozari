package com.viranika.sepasgozari

import android.annotation.SuppressLint
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        title = "نمایش وب"

        val url = intent.getStringExtra("url")

        if (isNetworkConnected()) {
            webView.settings.javaScriptEnabled = true
            webView.settings.builtInZoomControls = true
            webView.settings.textZoom = 100
            webView.webViewClient = WebViewClient()
            webView.loadUrl(url)
        } else {
            val alert = AlertDialog.Builder(this, R.style.DialogTheme)
            alert.setTitle("خطا!")
            alert.setMessage("دستگاه شما به اینترنت متصل نیست، لطفا اینترنت دستگاه را روشن کرده و مجددا تلاش کنید")
            alert.setPositiveButton("تلاش مجدد") { _, _ ->
                val intent2 = Intent(this, RefreshWebView::class.java)
                intent2.putExtra("address", url)
                startActivity(intent2)
            }
            alert.setNegativeButton("بیخیال") { _, _ ->
                val intent = Intent(this, ProgrammerActivity::class.java)
                startActivity(intent)
                finish()
            }
            alert.setCancelable(false)
            alert.show()
        }


    }

    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo
        return ni != null
    }

    override fun onBackPressed() {
        val intent = Intent(this, ProgrammerActivity::class.java)
        startActivity(intent)
    }

}
