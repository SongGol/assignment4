package com.example.assignment4

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService : Service() {
    lateinit var mPlayer: MediaPlayer

    companion object {
        val MESSAGE_KEY = "message_key"
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        mPlayer = MediaPlayer.create(this, R.raw.pahellbell_canon)
        mPlayer.isLooping = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mPlayer.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPlayer.stop()
    }
}