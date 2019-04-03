package com.viranika.sepasgozari

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import kotlinx.android.synthetic.main.start_layout.*
import kotlinx.android.synthetic.main.tool_start.view.*

class StartActivity : AppCompatActivity() {

    var settings: SharedPreferences? = null
    private var oneStart: SharedPreferences? = null
    private var days: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_layout)

        settings = getSharedPreferences("pref_main", Context.MODE_PRIVATE)
        oneStart = getSharedPreferences("oneStart", Context.MODE_PRIVATE)
        days = getSharedPreferences("days", Context.MODE_PRIVATE)

        setPref()

        val test = oneStart?.getBoolean("one", true)
        if (test!!) {
            for (item in 1..28) {
                val editor = days?.edit()
                editor?.putBoolean("$item", false)
                editor?.apply()
            }
            val editor = oneStart?.edit()
            editor?.putBoolean("one", false)
            editor?.apply()
        }

        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL

        toolbarStart.txtToolStart.text = "دوره سپاسگزاری"

        title = ""

    }

    override fun onResume() {
        super.onResume()

        setDayBack()

    }

    fun setDayBack() {
        for (item in 1..28) {
            val test = days?.getBoolean("$item", false)
            if (test!!) {
                when (item) {
                    4 -> button1.background = resources.getDrawable(R.drawable.shape2)
                    3 -> button2.background = resources.getDrawable(R.drawable.shape2)
                    2 -> button3.background = resources.getDrawable(R.drawable.shape2)
                    1 -> button4.background = resources.getDrawable(R.drawable.shape2)
                    8 -> button5.background = resources.getDrawable(R.drawable.shape2)
                    7 -> button6.background = resources.getDrawable(R.drawable.shape2)
                    6 -> button7.background = resources.getDrawable(R.drawable.shape2)
                    5 -> button8.background = resources.getDrawable(R.drawable.shape2)
                    12 -> button9.background = resources.getDrawable(R.drawable.shape2)
                    11 -> button10.background = resources.getDrawable(R.drawable.shape2)
                    10 -> button11.background = resources.getDrawable(R.drawable.shape2)
                    9 -> button12.background = resources.getDrawable(R.drawable.shape2)
                    16 -> button13.background = resources.getDrawable(R.drawable.shape2)
                    15 -> button14.background = resources.getDrawable(R.drawable.shape2)
                    14 -> button15.background = resources.getDrawable(R.drawable.shape2)
                    13 -> button16.background = resources.getDrawable(R.drawable.shape2)
                    20 -> button17.background = resources.getDrawable(R.drawable.shape2)
                    19 -> button18.background = resources.getDrawable(R.drawable.shape2)
                    18 -> button19.background = resources.getDrawable(R.drawable.shape2)
                    17 -> button20.background = resources.getDrawable(R.drawable.shape2)
                    24 -> button21.background = resources.getDrawable(R.drawable.shape2)
                    23 -> button22.background = resources.getDrawable(R.drawable.shape2)
                    22 -> button23.background = resources.getDrawable(R.drawable.shape2)
                    21 -> button24.background = resources.getDrawable(R.drawable.shape2)
                    28 -> button25.background = resources.getDrawable(R.drawable.shape2)
                    27 -> button26.background = resources.getDrawable(R.drawable.shape2)
                    26 -> button27.background = resources.getDrawable(R.drawable.shape2)
                    else -> button28.background = resources.getDrawable(R.drawable.shape2)
                }
            }
        }
    }

    fun onClickShow(view: View) {
        val ID = when (view.id) {
            R.id.button1 -> "4"
            R.id.button2 -> "3"
            R.id.button3 -> "2"
            R.id.button4 -> "1"
            R.id.button5 -> "8"
            R.id.button6 -> "7"
            R.id.button7 -> "6"
            R.id.button8 -> "5"
            R.id.button9 -> "12"
            R.id.button10 -> "11"
            R.id.button11 -> "10"
            R.id.button12 -> "9"
            R.id.button13 -> "16"
            R.id.button14 -> "15"
            R.id.button15 -> "14"
            R.id.button16 -> "13"
            R.id.button17 -> "20"
            R.id.button18 -> "19"
            R.id.button19 -> "18"
            R.id.button20 -> "17"
            R.id.button21 -> "24"
            R.id.button22 -> "23"
            R.id.button23 -> "22"
            R.id.button24 -> "21"
            R.id.button25 -> "28"
            R.id.button26 -> "27"
            R.id.button27 -> "26"
            else -> "25"
        }
        val intent = Intent(this, DaysActivity::class.java)
        intent.putExtra("ID", ID)
        startActivity(intent)
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

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
