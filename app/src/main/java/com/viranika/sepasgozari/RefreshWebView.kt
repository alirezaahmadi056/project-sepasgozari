package com.viranika.sepasgozari

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class RefreshWebView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = intent.getStringExtra("address")
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra("url", data)
        startActivity(intent)

    }
}
