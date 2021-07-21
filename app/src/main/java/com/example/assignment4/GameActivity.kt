package com.example.assignment4

import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.assignment4.databinding.ActivityGameBinding




class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private var pauseDialog = PauseDialog()
    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)

        val point = Point()
        if (Build.VERSION.SDK_INT >= 30) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
            val bound = windowManager.currentWindowMetrics.bounds
            gameView = GameView(this, bound.right - bound.left, bound.top - bound.bottom)
            Log.d("GameActivity ", bound.left.toString() + ", " + bound.top.toString() + ", " + bound.right.toString() + ", " + bound.bottom.toString())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            windowManager.defaultDisplay.getSize(point)
            gameView = GameView(this, point.x, point.y)
            Log.d("GameActivity x", point.x.toString() + ", y:" + point.y.toString())
        }

        binding.gameFrameLo.addView(gameView)
        binding.gameConstraintLo.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val action = event?.action
                val curX = event?.x //눌린 곳의 X좌표
                val curY = event?.y //눌린 곳의 Y좌표

                if (action == MotionEvent.ACTION_DOWN) {   //처음 눌렸을 때
                    Log.d("손가락 눌림","$curX, $curY")
                    if(gameView.mTile.isTouchIn(curX?.toInt(), curY?.toInt())) {
                        gameView.mTile.isClicked = true
                    }
                } else if (action == MotionEvent.ACTION_MOVE) {    //누르고 움직였을 때
                    Log.d("손가락 움직임", "$curX, $curY")
                } else if (action == MotionEvent.ACTION_UP) {    //누른걸 뗐을 때
                    Log.d("손가락 뗌", "$curX, $curY")
                }
                return true
            }
        })

        setContentView(binding.root)


        //Dialog 띄우기
        pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
            override fun onSelectSet(type: String) {
                Log.d("GameActivity get value", type)
                when(type) {
                    "return" -> finish()
                }
            }
        })
        binding.gamePauseIv.setOnClickListener {
            pauseDialog.setStyle( DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light );
            pauseDialog.show(supportFragmentManager, "selectDialog")
        }

    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }
}