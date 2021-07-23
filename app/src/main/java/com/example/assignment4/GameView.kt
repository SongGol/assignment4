package com.example.assignment4

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.*
import android.widget.ImageView
import java.util.*
import kotlin.collections.ArrayList

class GameView(var mContext: Context, var screenX: Int = 0, var screenY: Int = 0) : SurfaceView(mContext) , SurfaceHolder.Callback, Runnable {
    private val mHolder = holder
    private lateinit var mThread: GameThread
    var tiles = ArrayList<Tile>()
    var trash = ArrayList<Tile>()
    private var paint = Paint()

    private var mListener: OnExitListener? = null
    private val startTime: Long = System.currentTimeMillis()
    private var speed: Int = 10
    private lateinit var tThread: Thread
    private var isExit = false
    private var isPlaying = false
    private var background1: Background
    private var background2: Background
    var mTile: Tile

    companion object {
        var screenRatioX: Float = 0f
        var screenRatioY: Float = 0f
        const val NAME = "name"
        const val SCORE = "score"
    }

    init {
        mHolder.addCallback(this)

        screenRatioX = 1080f / screenX
        screenRatioY = 1920f / screenY

        background1 = Background(screenX, screenY, resources, R.drawable.blue_backgound)
        background2 = Background(screenX, screenY, resources, R.drawable.blue_backgound2)

        background2.y = screenY

        mTile = Tile(res = resources)
        tiles.add(Tile(res = resources))
    }

    override fun run() {
        while (isPlaying) {
            if (tiles[tiles.size - 1].y > 0) {
                when (Random().nextInt(10)) {
                    0, 6 -> tiles.add(Tile(res = resources, type = 0))
                    1, 7 -> tiles.add(Tile(res = resources, type = 1))
                    2, 8 -> tiles.add(Tile(res = resources, type = 2))
                    3, 9 -> tiles.add(Tile(res = resources, type = 3))
                    4 -> {
                        tiles.add(Tile(res = resources, type = 0))
                        tiles.add(Tile(res = resources, type = 2))
                    }
                    5 -> {
                        tiles.add(Tile(res = resources, type = 1))
                        tiles.add(Tile(res = resources, type = 3))
                    }
                }
            }

            //클릭되지 않은 타일이 내려왔을 때
            if (!tiles[0].isClicked && tiles[0].y + tiles[0].height > screenY) {
                Log.d("GameView", "tile isn't clicked")
                isPlaying = false
                mListener?.onExitSet()

                val intent = Intent(mContext, EndActivity::class.java)
                intent.putExtra(NAME, "test_name")
                intent.putExtra(SCORE, GameActivity.score)
                mContext.startActivity(intent)
                break
            }
            if (tiles.size >= 2 && tiles[0].y == tiles[1].y && tiles[1].y + tiles[1].height > screenY && !tiles[1].isClicked) {
                Log.d("GameView", "tile isn't clicked")
                isPlaying = false
                mListener?.onExitSet()

                val intent = Intent(mContext, EndActivity::class.java)
                intent.putExtra(NAME, "test_name")
                intent.putExtra(SCORE, GameActivity.score)
                mContext.startActivity(intent)
                break
            }

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

        mTile.y += (speed * screenRatioY).toInt()
        if (mTile.y > screenY) mTile.y = -mTile.height

        for (item in tiles) {
            item.y += (speed * screenRatioY).toInt()
            if (item.y > screenY) trash.add(item)
        }

        for (item in trash) {
            tiles.remove(item)
        }
        trash.clear()
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

            //drawLine(canvas, paint, screenX.toFloat(), screenY.toFloat())

            //canvas.drawBitmap(mTile.getTile(), mTile.x.toFloat(),  mTile.y.toFloat(), paint)

            for (item in tiles) {
                canvas.drawBitmap(item.getTile(), item.x.toFloat(),  item.y.toFloat(), paint)
            }

            mHolder.unlockCanvasAndPost(canvas)
        }
    }

    private fun sleep() {
        try {
            Thread.sleep(1)
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
        //mThread.join()
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

        if (event?.action == MotionEvent.ACTION_BUTTON_PRESS) {
            Log.d("GameView onTouchEvent()", "button pressed")
        }

        if (event?.action == MotionEvent.ACTION_DOWN) {
            synchronized(mHolder) {
                //tiles.add(Tile(5.0f, 5.0f))
            }
            true
        }
        false
    }

    inner class GameThread() : Thread() {
        var bExit = false

        override fun run() {
            super.run()
            if (tiles[tiles.size - 1].y > 0) {
                when (Random().nextInt(10)) {
                    0, 6 -> tiles.add(Tile(res = resources, type = 0))
                    1, 7 -> tiles.add(Tile(res = resources, type = 1))
                    2, 8 -> tiles.add(Tile(res = resources, type = 2))
                    3, 9 -> tiles.add(Tile(res = resources, type = 3))
                    4 -> {
                        tiles.add(Tile(res = resources, type = 0))
                        tiles.add(Tile(res = resources, type = 2))
                    }
                    5 -> {
                        tiles.add(Tile(res = resources, type = 1))
                        tiles.add(Tile(res = resources, type = 3))
                    }
                }
            }
        }
    }

    interface OnExitListener {
        fun onExitSet()
    }

    fun setOnExitListener(listener: OnExitListener) {
        mListener = listener
    }
}