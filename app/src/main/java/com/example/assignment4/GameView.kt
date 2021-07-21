package com.example.assignment4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.Display
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.lang.Exception

class GameView(mContext: Context, var screenX: Int = 0, var screenY: Int = 0) : SurfaceView(mContext) , SurfaceHolder.Callback, Runnable {
    private val mHolder = holder
    private lateinit var mThread: GameThread
    private var tiles = ArrayList<Tile>()
    private var paint = Paint()

    private lateinit var tThread: Thread
    private var isPlaying = false
    private var background1: Background
    private var background2: Background
    private var screenRatioX: Float = 0f
    private var screenRatioY: Float = 0f

    init {
        mHolder.addCallback(this)

        screenRatioX = 1080f / screenX
        screenRatioY = 1920f / screenY

        background1 = Background(screenX, screenY, resources, R.drawable.blue_backgound)
        background2 = Background(screenX, screenY, resources, R.drawable.blue_backgound2)

        background2.y = screenY

    }

    override fun run() {

        while (isPlaying) {
            update()
            draw()
            sleep()
        }
    }

    private fun update() {
        background1.y -= (10 * screenRatioY).toInt()
        background2.y -= (10 * screenRatioY).toInt()

        if (background1.y + background1.background.height < 0) {
            background1.y = screenY
        }

        if (background2.y + background2.background.height < 0) {
            background2.y = screenY
        }

    }

    private fun draw() {
        if (mHolder.surface.isValid) {
            val canvas: Canvas = mHolder.lockCanvas()

            val rect1 = Rect(0, 0, background1.x, background1.y)
            val rect2 = Rect(0, 0, background2.x, background2.y)
            //canvas.drawBitmap(background1.background, null, rect1, paint)
            //canvas.drawBitmap(background2.background, null, rect2, paint)
            canvas.drawBitmap(background1.background, background1.x.toFloat(), background1.y.toFloat(), paint)
            canvas.drawBitmap(background2.background, background2.x.toFloat(), background2.y.toFloat(), paint)

            mHolder.unlockCanvasAndPost(canvas)
        }
    }

    private fun sleep() {
        try {
            Thread.sleep(17)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun resume() {
        isPlaying = true
        tThread = Thread(this)
        tThread.start()
    }

    fun pause() {

        try {
            isPlaying = false
            tThread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d("GameView", "SurfaceCreated()")
        mThread = GameThread()
        var c: Canvas? = null

        /*
        try {
            c = mHolder.lockCanvas()
            synchronized(mHolder) {
                val paint = Paint()
                paint.setColor(Color.RED)
                c.drawRect(0f, 0f, 100f, 100f, paint)
            }
        } finally {
            if (c != null) mHolder.unlockCanvasAndPost(c)
        }

        val mTestThread = TestThread()
        mTestThread.start( ) */
        //mThread.start()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d("GameView", "SurfaceDestroyed()")
        mThread.bExit = true
        /*
        while(true) {
            try {
                mThread.join()
                break
            } catch (e: Exception) {
                Log.d("GameView surfaceDestroyed", e.toString())
            }
        }*/
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d("GameView", "SurfaceChanged()")
        if (mThread != null) {
            Log.d("GameView", "surfaceChanged()")
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
        Log.d("GameView onTouchEvent()", event?.action.toString())

        if (event?.action == MotionEvent.ACTION_DOWN) {
            synchronized(mHolder) {
                tiles.add(Tile(5.0f, 5.0f))
            }
            true
        }
        false
    }

    inner class TestThread(): Thread() {
        override fun run() {
            super.run()
            var canvas: Canvas
            for (i in 1..100) {
                synchronized(mHolder) {
                    canvas = mHolder.lockCanvas()

                    val paint = Paint()
                    paint.setColor(Color.RED)
                    canvas.drawRect(0f + i.toFloat(), 0f + i.toFloat(), 100f + i.toFloat(), 100f + i.toFloat(), paint)

                    mHolder.unlockCanvasAndPost(canvas)
                }
            }

        }
    }

    inner class GameThread() : Thread() {
        var bExit = false

        override fun run() {
            super.run()
            var canvas: Canvas

            ex@while (!bExit) {
                for (item in tiles) {
                    item.move()

                }

                synchronized(mHolder) {
                    canvas = mHolder.lockCanvas()

                    for (item in tiles) {
                        item.draw(canvas)
                    }


                    mHolder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }
}