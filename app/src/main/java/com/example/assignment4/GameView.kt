package com.example.assignment4

import android.content.Context
import android.graphics.Canvas
import android.view.Display
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(mContext: Context) : SurfaceView(mContext) , SurfaceHolder.Callback {
    private val mHolder = holder

    init {
    }
    override fun surfaceCreated(holder: SurfaceHolder) {
        TODO("Not yet implemented")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        TODO("Not yet implemented")
    }

    inner class GameThread(var bExit : Boolean = false, var mContext: Context) : Thread() {

        init {
            val display: Display = mContext.getSystemService()
        }

        override fun run() {
            var canvas: Canvas

            loop@while (!bExit) {

                synchronized(mHolder) {
                    canvas = mHolder.lockCanvas()
                    if (canvas == null) loop@break

                    mHolder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }
}