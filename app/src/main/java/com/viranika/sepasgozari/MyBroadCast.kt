package com.viranika.sepasgozari

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer

class MyBroadCast : BroadcastReceiver() {

    var media: MediaPlayer? = null

    override fun onReceive(context: Context?, intent: Intent?) {

        media = MediaPlayer.create(context, R.raw.sounds_notification)
        media?.start()

        val intent = Intent(context, StartActivity::class.java)
        val pending = PendingIntent.getActivity(context, 0, intent, 0)
        val note = Notification.Builder(context)
                .setTicker("")
                .setContentTitle("معجزه سپاسگزاری")
                .setContentText("زمان خواندن مطالب مربوط به امروز فرا رسیده است")
                .setSmallIcon(R.drawable.if_office)
                .setContentIntent(pending).notification
        note.flags = Notification.FLAG_AUTO_CANCEL
        val nm = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(0, note)

    }

}