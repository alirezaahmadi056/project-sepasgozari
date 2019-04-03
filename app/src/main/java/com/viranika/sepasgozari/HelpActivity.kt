package com.viranika.sepasgozari

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_days.*
import kotlinx.android.synthetic.main.activity_help.*
import kotlinx.android.synthetic.main.toolbar.view.*

class HelpActivity : AppCompatActivity() {

    var settings: SharedPreferences? = null
    var textData: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        webHelp.setBackgroundColor(0x00000000)

        settings = getSharedPreferences("pref_main", Context.MODE_PRIVATE)

        title = ""

        toolbarCreate.txtTool.text = "راهنمای اپلیکیشن"

        //Setting up project startup entries
        val text = resources.openRawResource(R.raw.help)
        textData = HelperConvert.convertStreamToString(text)

    }

    override fun onResume() {
        super.onResume()

        setPref()

    }

    /**
     * Load in Data Preferences And Change Objects
     */
    private fun setPref() {
        settings = getSharedPreferences("pref_main", Context.MODE_PRIVATE)
        setFont()
        setSleepScreen()
    }

    fun startPrefHelp(view: View) {
        val intent = Intent(this, PrefActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private fun setSleepScreen() {
        if (settings?.getBoolean("check_sleep", false)!!)
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun setFont() {

        //Set Font Size
        val size = settings?.getString("font_size", "20")!!

        //Set Font Family
        val path = settings?.getString("font_family", "font/yekanweb-regular.ttf")!!

        setWebView(size, path)
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun setWebView(size: String, font: String) {
        val pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/" + font + "\")}body {font-family: MyFont;font-size: " + size + "px;text-align: justify;}</style></head><body dir='rtl' align='right'>"
        val pas = "</body></html>"
        textData?.trimEnd()
        val myHtmlString = pish + textData + pas
        webHelp.loadDataWithBaseURL(null, myHtmlString, "text/html", "utf-8", null)
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
