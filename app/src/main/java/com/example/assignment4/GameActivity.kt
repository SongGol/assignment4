package com.example.assignment4

import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
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

        setContentView(gameView)


        //Dialog 띄우기
        pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
            override fun onSelectSet(type: String) {
                Log.d("GameActivity get value", type)
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