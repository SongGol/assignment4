package com.example.assignment4

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log

class MusicService : Service() {

    companion object {
        var mPlayer: MediaPlayer? = null

        const val MKEY = "music_name"
        const val ALONE = "Alone"
        const val FADED = "Faded"
        const val SPECTRE = "The Spectre"
        const val TIGER = "Eye Of The Tiger"
        const val HAPPY = "Happy New Year"
        const val JINGLE = "징글벨"
        const val STAR = "반짝반짝 작은 별"

        var music = R.raw.pahellbell_canon

        fun start() {
            mPlayer?.start()
        }

        fun pause() {
            mPlayer?.pause()
        }
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("MusicService","onCreate()")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val musicName = intent?.extras?.getString(MKEY)
        music = when (musicName) {
            ALONE -> R.raw.alan_walker_alone
            FADED -> R.raw.alan_walker_faded
            SPECTRE -> R.raw.alan_walker_the_spectre
            TIGER -> R.raw.eye_of_the_tiger_survivor
            HAPPY -> R.raw.happy_new_year_abba
            JINGLE -> R.raw.jingle_bell
            STAR -> R.raw.twinkle_little_star
            else -> R.raw.pahellbell_canon
        }
        mPlayer = MediaPlayer.create(this, music)
        mPlayer?.isLooping = false
        mPlayer?.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPlayer?.stop()
    }
}