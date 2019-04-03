package com.viranika.sepasgozari

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BroadCastDay : BroadcastReceiver() {

    @SuppressLint("CommitPrefEdits")
    override fun onReceive(context: Context, intent: Intent) {
        val db = DBHandler(context)

        db.openDB()

        val result = db.queryTblDayCurrent()
        val list = hashMapOf<String, Int>()
        if (result.moveToNext()) {
            list["one"] = result.getInt(1)
            list["two"] = result.getInt(2)
            list["three"] = result.getInt(3)
        }
        var dayCurrent = list["one"]
        var dayOff = list["two"]
        var term = list["three"]
        list.clear()
        if (dayCurrent != null && dayOff != null && term != null) {
            if (dayCurrent < 28) {
                dayCurrent += 1
                dayOff -= 1
            } else {
                term += 1
                dayCurrent = 0
                dayOff = 28
            }
            db.updateDayCurrentCast(dayCurrent, dayOff, term)
        }

        db.close()

    }
}
