package com.viranika.sepasgozari

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_days.*
import kotlinx.android.synthetic.main.toolbar.view.*

class DaysActivity : AppCompatActivity() {

    private var db: DBHandler? = null
    private var desc: String? = null
    private var count: String? = null
    var settings: SharedPreferences? = null
    private var days: SharedPreferences? = null
    private var media: MediaPlayer? = null
    private var textData: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_days)

        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        webDesc.setBackgroundColor(0x00000000)

        days = getSharedPreferences("days", Context.MODE_PRIVATE)
        settings = getSharedPreferences("pref_main", Context.MODE_PRIVATE)
        count = intent.getStringExtra("ID")

        val check = settings?.getBoolean("sound", true)
        if (check!!) {
            media = MediaPlayer.create(this, R.raw.sound_back)
            media?.start()
        }

        //Change Title
        title = ""

        checkDayActive()

        db = DBHandler(this)
        db?.openDB()
        val result = db?.queryTblDays()
        setData(result!!, count?.toInt()!!)
        db?.close()

    }

    override fun onResume() {
        super.onResume()

        checkFav()
        webDesc.clearSslPreferences()
        setPref()

    }

    override fun onRestart() {
        super.onRestart()

        val check = settings?.getBoolean("sound", true)
        if (check!!) {
            media = MediaPlayer.create(this, R.raw.sound_back)
            media?.start()
        }

    }

    override fun onStop() {
        super.onStop()

        if (media != null)
            if (media?.isPlaying!!)
                media?.stop()

    }

    fun checkDayActive() {
        val test = days?.getBoolean("$count", false)
        if (test!!) {
            return
        } else {
            val editor = days?.edit()
            editor?.putBoolean("$count", true)
            editor?.apply()
        }
    }

    fun startPrefDays(view: View) {
        val intent = Intent(this, PrefActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    fun favClick(view: View) {
        val db2 = DBHandler(this)
        db2.openDB()
        val column = arrayOf("fav")
        val selectionArgs = arrayOf(count!!)
        val cursor = db2.runQuery(column, "DayID LIKE ?", selectionArgs)
        if (cursor.moveToFirst()) {
            val fav1 = cursor.getString(cursor.getColumnIndex("fav"))
            if (fav1 == "true") {
                imgFavDay.setImageResource(R.drawable.if_not_fav)
                val test = db2.update(count!!, "false")
                if (test != null) {
                    if (test > 0) {
                        Toast.makeText(this, "مطلب از علاقه مندی ها حذف شد", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "خطا در عملیات", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                imgFavDay.setImageResource(R.drawable.if_fav)

                val test = db2.update(count!!, "true")
                if (test != null) {
                    if (test > 0) {
                        Toast.makeText(this, "مطلب به علاقه مندی ها اضافه شد", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "خطا در عملیات", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        db2.close()
    }

    fun checkFav() {
        val db2 = DBHandler(this)
        db2.openDB()
        val column = arrayOf("fav")
        val selectionArgs = arrayOf(count!!)
        val cursor = db2.runQuery(column, "DayID LIKE ?", selectionArgs)
        if (cursor.moveToFirst()) {
            val fav1 = cursor.getString(cursor.getColumnIndex("fav"))
            if (fav1 == "true")
                imgFavDay.setImageResource(R.drawable.if_fav)
            else
                imgFavDay.setImageResource(R.drawable.if_not_fav)
        }
        db2.close()
    }


    /**
     * Load in Data Preferences And Change Objects
     */
    private fun setPref() {
        settings = getSharedPreferences("pref_main", Context.MODE_PRIVATE)
        setFont()
        setSleepScreen()
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
        webDesc.loadDataWithBaseURL(null, myHtmlString, "text/html", "utf-8", null)
    }

    /**
     * Extract texts from the database
     */
    private fun setData(cursor: Cursor, ID: Int) {
        val list = hashMapOf<String, String>()
        val list2 = hashMapOf<String, String>()
        val list3 = hashMapOf<String, String>()
        var id = 1
        while (cursor.moveToNext()) {
            list["$id"] = cursor.getString(1)
            list2["$id"] = cursor.getString(2)
            list3["$id"] = cursor.getString(4)
            id++
        }
        txtDay.text = list3["$ID"] + " :"
        toolbarDay.txtTool.text = list["$ID"]
        /*val sp = Html.fromHtml(list2.get(key = "$ID"))
        txtDesc.text = sp*/
        textData = list2.get(key = "$ID")
        desc = list2["$ID"]
        list.clear()
        list2.clear()
    }

    /**
     * onClick-sharing method of the article
     */
    fun shareClick(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "subject")
        intent.putExtra(Intent.EXTRA_TEXT, "$desc")
        startActivity(Intent.createChooser(intent, "sending"))
    }

}
