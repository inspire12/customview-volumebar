package com.example.customview

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

//    private lateinit var audioManager: AudioManager
    private var total = 100
    private var value = total/2
    private var visibleDot = 5
    private var interval = total/ visibleDot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        btnPrev.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                volumeBar.prevPage()
            }
        })
        btnNext.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
              volumeBar.nextPage()
            }
        })

        volumeBar.calibrateLevels(

                value)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        super.dispatchKeyEvent(event)
//        if (event?.keyCode == KeyEvent.KEYCODE_VOLUME_UP ||
//                event?.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//            volumeBar.setVolumeLevel(audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM))
//        }
        return false
    }
}
