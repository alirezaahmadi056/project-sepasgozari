package com.viranika.sepasgozari

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.activity_maiin.*
import kotlinx.android.synthetic.main.mention_layout.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var settings: SharedPreferences? = null
    private var oneStart: SharedPreferences? = null
    var oldCurrentTimeMillis = 0L
    val time_interval = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maiin)

        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL

        val toolbar = findViewById<Toolbar>(R.id.toolbarCreate)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        title = ""

        settings = getSharedPreferences("pref_main", Context.MODE_PRIVATE)
        oneStart = getSharedPreferences("oneStart", Context.MODE_PRIVATE)

    }

    override fun onResume() {
        super.onResume()

        setDay()
        setPref()

    }

    @SuppressLint("SetTextI18n")
    fun setDay() {

        val db = DBHandler(this)
        db.openDB()

        val result = db.queryTblDayCurrent()
        val list = hashMapOf<String, Int>()
        if (result.moveToNext()) {
            list["one"] = result.getInt(1)
            list["two"] = result.getInt(2)
            list["three"] = result.getInt(3)
        }
        val dayCurrent = list["one"]
        val dayOff = list["two"]
        val term = list["three"]
        list.clear()
        txtDayCurrent.text = dayCurrent?.toString() + " "
        txtDayOff.text = dayOff?.toString() + " "
        txtTerm.text = term?.toString() + " "

        db.close()

    }

    @SuppressLint("CommitPrefEdits")
            /**
             * Clicking all button methods based on id
             */
    fun onClickStart(view: View) {
        when (view.id) {
            R.id.view_help -> {
                val intent = Intent(this, HelpActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
            R.id.view_start -> {
                val test = oneStart?.getBoolean("one", true)
                if (test!!) {
                    val dialog = AlertDialog.Builder(this, R.style.DialogTheme)
                    dialog.setTitle("شروع دوره")
                    dialog.setMessage("با فشردن تایید، دوره آغاز خواهد شد")
                    dialog.setPositiveButton("تایید") { _, _ ->
                        startTerm()
                        val intent = Intent(this, StartActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    }
                    dialog.setNegativeButton("لغو") { _, _ ->

                    }
                    dialog.setCancelable(false)
                    dialog.show()
                } else {
                    val intent = Intent(this, StartActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }
            }
            R.id.view_fav -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
            R.id.view_create_app -> {
                val intent = Intent(this, CreateActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    @SuppressLint("PrivateResource", "InflateParams", "CommitPrefEdits")
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item != null) {
            when (item.itemId) {
                R.id.pref -> {
                    val intent = Intent(this, PrefActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }
                R.id.programmer -> {
                    val intent = Intent(this, ProgrammerActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }
                R.id.mention -> {
                    val inflater = layoutInflater
                    val view: View = inflater.inflate(R.layout.mention_layout, null)
                    val alert = AlertDialog.Builder(this, R.style.DialogTheme)
                    alert.setTitle("یادآوری")
                    alert.setView(view)
                    alert.setPositiveButton("تنظیم") { _, _ ->
                        val hours = view.time.currentHour
                        val minute = view.time.currentMinute

                        // Set the alarm to start
                        val calendar: Calendar = Calendar.getInstance()
                        calendar.set(Calendar.HOUR_OF_DAY, hours)
                        calendar.set(Calendar.MINUTE, minute)

                        val intent = Intent(this, MyBroadCast::class.java)
                        val pending = PendingIntent.getBroadcast(applicationContext, 1234, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                        val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                        // With setInexactRepeating(), you have to use one of the AlarmManager interval
                        // constants--in this case, AlarmManager.INTERVAL_DAY.
                        alarm.setInexactRepeating(
                                AlarmManager.RTC_WAKEUP,
                                calendar.timeInMillis,
                                AlarmManager.INTERVAL_DAY,
                                pending
                        )
                        Toast.makeText(this, "هشدار ایجاد شد", Toast.LENGTH_SHORT).show()
                    }
                    alert.setNegativeButton("بازگشت") { _, _ ->

                    }
                    alert.setCancelable(false)
                    alert.show()
                }
                R.id.restart -> {
                    val alert = AlertDialog.Builder(this, R.style.DialogTheme)
                    alert.setTitle("راه اندازی مجدد")
                    alert.setMessage("لطفا قبل از راه اندازی مجدد، قسمت راهنمایی را مطالعه کنید")
                    alert.setPositiveButton("راه اندازی مجدد") { _, _ ->
                        val editor = oneStart?.edit()
                        editor?.putBoolean("one", true)
                        editor?.apply()

                        val db = DBHandler(this)
                        db.openDB()
                        db.updateDayCurrent(0, 28)
                        db.close()
                        setDay()
                    }
                    alert.setNegativeButton("بازگشت") { _, _ ->

                    }
                    alert.setCancelable(false)
                    alert.show()

                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun startTerm() {
        val hoursSystem = getTimeSystem("HH")
        val minuteSystem = getTimeSystem("mm")
        val secondSystem = getTimeSystem("ss")
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hoursSystem)
        calendar.set(Calendar.MINUTE, minuteSystem)
        calendar.set(Calendar.SECOND, secondSystem)
        val intent = Intent(this, BroadCastDay::class.java)
        val pending = PendingIntent.getBroadcast(applicationContext, 5678, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        alarm.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pending
        )
    }

    /**
     * Getting the system time based on the input of the function
     */
    @SuppressLint("SimpleDateFormat")
    fun getTimeSystem(simple: String): Int {
        val sfg: Long = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat(simple)
        return dateFormat.format(sfg).toInt()
    }

    /**
     * Load in Data Preferences And Change Objects
     */
    private fun setPref() {
        settings = getSharedPreferences("pref_main", Context.MODE_PRIVATE)
        setFontSize()
        setSleepScreen()
    }

    private fun setSleepScreen() {
        if (settings?.getBoolean("check_sleep", false)!!)
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun setFontSize() {

        //Set Font Family
        val path = "font/yekanweb-regular.ttf"
        val rr: ConstraintLayout = findViewById(R.id.main_activity)
        val typeface = Typeface.createFromAsset(assets, path)
        setFontFamily(rr, typeface)
        val typeface2 = Typeface.createFromAsset(assets, "font/yekanweb-regular.ttf")
        toolbarCreate.txtTool.typeface = typeface2
    }

    private fun setFontFamily(group: ViewGroup, font: Typeface) {
        val count = group.childCount
        var v: View
        for (i in 0 until count) {
            v = group.getChildAt(i)
            if (v is TextView || v is EditText || v is Button) {
                (v as TextView).typeface = font
            } else if (v is ViewGroup)
                setFontFamily(v, font)
        }
    }

    override fun onBackPressed() {
        if (oldCurrentTimeMillis + time_interval > System.currentTimeMillis()) {
            finishAffinity()
            return
        } else {
            Toast.makeText(this, "برای خروج دوباره کلیک کنید", Toast.LENGTH_SHORT).show()
        }
        oldCurrentTimeMillis = System.currentTimeMillis()
    }

}
