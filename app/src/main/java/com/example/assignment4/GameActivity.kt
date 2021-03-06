package com.example.assignment4

import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.assignment4.databinding.ActivityGameBinding




class GameActivity : AppCompatActivity(){
    private lateinit var binding: ActivityGameBinding
    private var pauseDialog = PauseDialog()
    private lateinit var gameView: GameView
    private var mHandler = Handler(Looper.getMainLooper())

    companion object {
        var score = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        score = 0
        binding.gameScoreTv.text = SharedPreferencesManager.getStrValue(this, GameView.NAME)

        val intent = Intent(this, MusicService::class.java)
        intent.putExtra(MusicService.MKEY, binding.gameScoreTv.text.toString())
        startService(intent)

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

        gameView.setOnExitListener(object : GameView.OnExitListener {
            override fun onExitSet() {
                Log.d("GameActivity","onExit")
                this@GameActivity.finish()
            }
        })

        binding.gameFrameLo.addView(gameView)
        binding.gameConstraintLo.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val action = event?.action
                val curX = event?.x //?????? ?????? X??????
                val curY = event?.y //?????? ?????? Y??????

                if (action == MotionEvent.ACTION_DOWN) {   //?????? ????????? ???
                    Log.d("????????? ??????","$curX, $curY")
                    var bTileCheck = false
                    for (index in 0..(gameView.tiles.size - 1)) {
                        val item = gameView.tiles[index]
                        if (item.isTouchIn(curX?.toInt(), curY?.toInt())) {
                            if (!item.isClicked) {
                                //score++
                                //mHandler.post{
                                //    binding.gameScoreTv.text = score.toString()
                                //}
                            }
                            if (item == gameView.tiles[0] ||
                                (gameView.tiles[index - 1].y != gameView.tiles[index].y && gameView.tiles[index - 1].isClicked) ||
                                (gameView.tiles[index - 1].y == gameView.tiles[index].y && gameView.tiles[index - 2].isClicked)) {
                                score++
                                mHandler.post{
                                    binding.gameScoreTv.text = score.toString()
                                }
                                item.isClicked = true
                                bTileCheck = true
                            }
                        }
                    }
                    //????????? ?????? ??? ??????
                    if (!bTileCheck) {
                        //gameView.clickWrong(curX?.toInt(), curY?.toInt())
                        //gameView.pause()
                    }
                } else if (action == MotionEvent.ACTION_MOVE) {    //????????? ???????????? ???
                    //Log.d("????????? ?????????", "$curX, $curY")
                } else if (action == MotionEvent.ACTION_UP) {    //????????? ?????? ???
                    Log.d("????????? ???", "$curX, $curY")
                }
                return true
            }

        })

        setContentView(binding.root)


        //Dialog ?????????
        pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
            override fun onSelectSet(type: String) {
                Log.d("GameActivity get value", type)
                when(type) {
                    "start" ->  {
                        gameView.resume()
                        MusicService.start()
                    }
                    "restart" -> {
                        score = 0
                        binding.gameScoreTv.text = "0"
                        gameView.tiles = ArrayList<Tile>()
                        gameView.tiles.add(Tile(res = resources))
                        gameView.resume()

                        val intent = Intent(this@GameActivity, MusicService::class.java)
                        intent.putExtra(MusicService.MKEY, SharedPreferencesManager.getStrValue(this@GameActivity, GameView.NAME))
                        startService(intent)
                    }
                    "return" -> finish()
                }
            }
        })
        binding.gamePauseIv.setOnClickListener {
            pauseDialog.setStyle( DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light );
            pauseDialog.show(supportFragmentManager, "selectDialog")
            gameView.pause()
            MusicService.pause()
        }

    }

    override fun onRestart() {
        super.onRestart()
        Log.d("GameActivity", "onRestart()")
        MusicService.start()
    }

    override fun onPause() {
        super.onPause()
        Log.d("GameActivity", "onPause()")
        gameView.pause()

        MusicService.pause()
    }

    override fun onResume() {
        super.onResume()
        Log.d("GameActivity", "onResume()")
        gameView.resume()


    }

    override fun onBackPressed() {
        Log.d("GameActivity", "onBackPressed()")
        pauseDialog.setStyle( DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light );
        pauseDialog.show(supportFragmentManager, "selectDialog")
        gameView.pause()
        MusicService.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(this, MusicService::class.java)
        stopService(intent)
    }
}