package com.viranika.sepasgozari

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.temp_start.view.*
import kotlinx.android.synthetic.main.toolbar.view.*

class FavoriteActivity : AppCompatActivity() {

    private var db: DBHandler? = null
    private val arrayListFav = ArrayList<ValuesData>()
    private var settings: SharedPreferences? = null
    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL

        settings = getSharedPreferences("pref_main", Context.MODE_PRIVATE)

        setPref()

        title = ""

        toolbarCreate.txtTool.text = "مطالب مورد علاقه"

        db = DBHandler(this)
        db?.openDB()
        setData()
        db?.close()

    }

    override fun onResume() {
        super.onResume()

        db = DBHandler(this)
        db?.openDB()
        setData()
        db?.close()

    }

    private fun setData() {
        arrayListFav.clear()
        val column = arrayOf("DayID", "Title", "fav", "desc")
        val selectionArgs = arrayOf("true")
        val cursor = db?.runQuery(column, "fav Like ?", selectionArgs)
        if (cursor?.moveToFirst()!!) {
            do {
                val DayID = cursor.getInt(cursor.getColumnIndex("DayID"))
                val title = cursor.getString(cursor.getColumnIndex("Title"))
                val fav = cursor.getString(cursor.getColumnIndex("fav"))
                val desc = cursor.getString(cursor.getColumnIndex("desc"))
                arrayListFav.add(ValuesData(DayID, title, fav, desc))
            } while (cursor.moveToNext())
        }
        val adapter = MyAdapter(arrayListFav)
        listFavorite.adapter = adapter
    }

    inner class MyAdapter(var categores: List<ValuesData>) : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val categoryView: View
            val holder: ViewHolder
            if (convertView == null) {
                categoryView = LayoutInflater.from(context).inflate(R.layout.temp_start, null)
                holder = ViewHolder()
                holder.txtTitle = categoryView.findViewById(R.id.txtTitleStart)
                holder.txtDesc = categoryView.findViewById(R.id.txtDescStart)
                categoryView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
                categoryView = convertView
            }
            val category = categores[position]
            holder.txtTitle?.text = category.title + " :"
            holder.txtDesc?.text = category.desc
            val count = category.ID.toString()
            categoryView.imgFavStart.setImageResource(R.drawable.if_fav)
            categoryView.imgFavStart.setOnClickListener {
                val db2 = DBHandler(context)
                db2.openDB()
                val test = db2.update(count, "false")
                categoryView.imgFavStart.setImageResource(R.drawable.if_not_fav)
                if (test != null) {
                    if (test > 0) {
                        Toast.makeText(context, "مطلب از علاقه مندی ها حذف شد", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "خطا در عملیات", Toast.LENGTH_SHORT).show()
                    }
                }
                db2.close()
                val intent = Intent(context, Refresh::class.java)
                startActivity(intent)
            }
            categoryView.list_start_temp.setOnClickListener {
                val intent = Intent(this@FavoriteActivity, DaysActivity::class.java)
                intent.putExtra("ID", "${category.ID}")
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
            return categoryView
        }

        override fun getItem(position: Int): Any {
            return categores[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return categores.count()
        }
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

class ValuesData(val ID: Int, val title: String, val fav: String, val desc: String)

class ViewHolder {
    var txtTitle: TextView? = null
    var txtDesc: TextView? = null
}
