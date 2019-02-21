package com.example.henryjacobs.shoppinglist

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.app.Activity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.henryjacobs.shoppinglist.R.drawable.shopping
import kotlinx.android.synthetic.main.splash_layout.*


class SplashActivity : AppCompatActivity() {
    private val SPLASH_DISPLAY_LENGTH: Long = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_layout)
        YoYo.with(Techniques.FadeIn).duration(4000)
                .playOn(findViewById(R.id.splashscreen))

        startAnim()


        Handler().postDelayed({
            val mainIntent = Intent(this@SplashActivity, ScrollingActivity::class.java)
            this@SplashActivity.startActivity(mainIntent)
            this@SplashActivity.finish()
            stopAnim()
            finish()
        }, SPLASH_DISPLAY_LENGTH)

    }

    fun startAnim(){
        avi.show();
        // or avi.smoothToShow();
    }

    fun stopAnim(){
        avi.hide();
        // or avi.smoothToHide();
    }

}
