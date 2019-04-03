package com.viranika.sepasgozari

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_programmer.*
import kotlinx.android.synthetic.main.toolbar.view.*
import android.content.ActivityNotFoundException
import android.net.Uri

class ProgrammerActivity : AppCompatActivity() {

    var settings: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_programmer)

        title = ""

        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL

        settings = getSharedPreferences("pref_main", Context.MODE_PRIVATE)

        setPref()

        toolbarCreate.txtTool.text = ""

    }

    /**
     * Load in Data Preferences And Change Objects
     */
    private fun setPref() {
        settings = getSharedPreferences("pref_main", Context.MODE_PRIVATE)
        setSleepScreen()
    }

    private fun setSleepScreen() {
        if (settings?.getBoolean("check_sleep", false)!!)
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    fun openSite(view: View) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            val array = Array(1) { android.Manifest.permission.INTERNET }
            ActivityCompat.requestPermissions(this, array, 1234)
        } else {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("url", "http://www.viranika.com")
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    fun openIntent(view: View) {
        when (view.id) {
            R.id.imgTel -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/ViraNika_co"))
                startActivity(intent)
            }
            R.id.imgInsta -> {
                val uri = Uri.parse("http://instagram.com/viranika_co")
                val intent = Intent(Intent.ACTION_VIEW, uri)

                intent.setPackage("com.instagram.android")

                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    val intent1 = Intent(this, WebActivity::class.java)
                    intent1.putExtra("url", "http://instagram.com/viranika_co")
                    startActivity(intent1)
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1234) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(this, WebActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}