package com.example.assignment4

import android.content.Context
import android.graphics.Canvas
import android.util.Log
import android.view.Display
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.lang.Exception

class GameView(mContext: Context) : SurfaceView(mContext) , SurfaceHolder.Callback {
    private val mHolder = holder
    private lateinit var mThread: GameThread
    private var tiles = ArrayList<Tile>()

    init {
        mHolder.addCallback(this)
    }
    override fun surfaceCreated(holder: SurfaceHolder) {
        mThread = GameThread()
        mThread.start()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mThread.bExit = true
        while(true) {
            try {
                mThread.join()
                break
            } catch (e: Exception) {
                Log.d("GameView surfaceDestroyed", e.toString())
            }
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        if (mThread != null) {
            Log.d("GameView", "surfaceChanged()")
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    inner class GameThread() : Thread() {
        var bExit = false

        override fun run() {
            var canvas: Canvas

            ex@while (!bExit) {

                synchronized(mHolder) {
                    canvas = mHolder.lockCanvas()


                    mHolder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }
}