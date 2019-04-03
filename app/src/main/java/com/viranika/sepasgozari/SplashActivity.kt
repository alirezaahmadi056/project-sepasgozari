package com.viranika.sepasgozari

import android.content.Intent
import android.graphics.Typeface
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    var media: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val typyFace = Typeface.createFromAsset(assets, "font/IRANSansWeb_Medium.ttf")
        txt_splash_one.typeface = typyFace
        txt_splash_tow.typeface = typyFace

        val animation1 = AnimationUtils.loadAnimation(this, R.anim.move_right)
        val animation = AnimationUtils.loadAnimation(this, R.anim.move_left)
        val animation3 = AnimationUtils.loadAnimation(this, R.anim.move_top)
        val animation4 = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        txt_splash_tow.animation = animation
        txt_splash_one.animation = animation1
        txt_splash_three.animation = animation3
        splash_image_view.animation = animation4

        media = MediaPlayer.create(this, R.raw.sound)
        media?.start()

        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }, 7000)
    }

    override fun onStop() {
        super.onStop()
        if (media!!.isPlaying)
            media?.stop()
    }

}
