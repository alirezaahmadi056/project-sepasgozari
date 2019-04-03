package com.viranika.sepasgozari

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_pref.*
import kotlinx.android.synthetic.main.toolbar.view.*

class PrefActivity : AppCompatActivity() {

    var pref: SharedPreferences? = null
    val sizeSmall = "14"
    val sizeMedume = "20"
    val sizeLarge = "26"
    val fontYekan = "font/yekanweb-regular.ttf"
    val fontSans = "font/IRANSansWeb_Medium.ttf"
    val fontNazanin = "font/g.ttf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pref)

        pref = getSharedPreferences("pref_main", Context.MODE_PRIVATE)

        val check = pref?.getBoolean("sound", true)
        chbSound.isChecked = check!!
        val check2 = pref?.getBoolean("check_sleep", false)
        chbSleep.isChecked = check2!!

        title = ""

        setPref()

        val size = pref?.getString("font_size", "20")!!
        when (size) {
            "14" -> rdbSmall.isChecked = true
            "20" -> rdbMedume.isChecked = true
            else -> rdbLarge.isChecked = true
        }

        val font = pref?.getString("font_family", "font/yekanweb-regular.ttf")!!
        when (font) {
            fontSans -> rdbSans.isChecked = true
            fontYekan -> rdbYekan.isChecked = true
            fontNazanin -> rdbNazanin.isChecked = true
        }

        toolbar1.txtTool.text = "تنظیمات برنامه"

    }

    fun changePref(view: View) {
        when (view.id) {
            R.id.chbSleep -> {
                val editor = pref?.edit()
                editor?.putBoolean("check_sleep", chbSleep.isChecked)
                editor?.apply()
                setSleepScreen()
            }
            R.id.rdbSans -> {
                val editor = pref?.edit()
                editor?.putString("font_family", fontSans)
                editor?.apply()
                setFont()
            }
            R.id.rdbYekan -> {
                val editor = pref?.edit()
                editor?.putString("font_family", fontYekan)
                editor?.apply()
                setFont()
            }
            R.id.rdbNazanin -> {
                val editor = pref?.edit()
                editor?.putString("font_family", fontNazanin)
                editor?.apply()
                setFont()
            }
            R.id.rdbSmall -> {
                val editor = pref?.edit()
                editor?.putString("font_size", sizeSmall)
                editor?.apply()
                setFont()
            }
            R.id.rdbMedume -> {
                val editor = pref?.edit()
                editor?.putString("font_size", sizeMedume)
                editor?.apply()
                setFont()
            }
            R.id.rdbLarge -> {
                val editor = pref?.edit()
                editor?.putString("font_size", sizeLarge)
                editor?.apply()
                setFont()
            }
            R.id.chbSound -> {
                val editor = pref?.edit()
                editor?.putBoolean("sound", chbSound.isChecked)
                editor?.apply()
            }
        }
    }

    fun setPref() {
        setSleepScreen()
        setFont()
    }

    fun setFont() {
        val path = pref?.getString("font_family", "font/yekanweb-regular.ttf")!!
        val typeface = Typeface.createFromAsset(assets, path)
        txtTestFontPref.typeface = typeface

        txtTestFontPref.textSize = pref?.getString("font_size", "18")!!.toFloat()

    }

    fun setSleepScreen() {
        if (pref?.getBoolean("check_sleep", false)!!) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    override fun onBackPressed() {
        finish()
    }

}
