package com.example.assignment4

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment4.databinding.ActivityEndBinding

class EndActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEndBinding
    private var score: Int = 0

    private lateinit var mHandler: Handler
    private var heartCount: Int = 10
    private var heartTime: Int = 5
    private var mThread: Thread = Thread() {
        try {
            while (true) {
                Log.d("EndActivity", "check")
                if (heartTime > 0 && heartCount < 10) {
                    heartTime--
                } else {
                    heartTime = 5
                    if (heartCount < 10) heartCount++
                }

                mHandler.post {
                    binding.heartGetTimeTv.text = "00:0"+heartTime.toString()
                    binding.heartCountTv.text = heartCount.toString()
                }

                Thread.sleep(1000)
            }
        } catch (e: InterruptedException) {
            SharedPreferencesManager.putIntValue(this, HEART, heartCount)
            Log.d("EndActivity interrupt", heartCount.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mHandler = Handler(Looper.getMainLooper())

        val name = SharedPreferencesManager.getStrValue(this, GameView.NAME)
        score = GameActivity.score
        var maxScore = SharedPreferencesManager.getIntValue(this, GameView.SCORE)
        if (maxScore < score) maxScore = score

        binding.songTitleTv.text = name
        binding.resultScoreTv.text = score.toString()
        binding.maxScoreTv.text = String.format(resources.getString(R.string.max_score_tv, maxScore))
        binding.resultExpTv.text = String.format(resources.getString(R.string.result_exp_tv, 0))
        binding.resultCoinTv.text = String.format(resources.getString(R.string.result_coin_tv, score * 10))

        //상단 숫자 설정
        heartCount = SharedPreferencesManager.getIntValue(this, HEART)
        Log.d("EndActivity onCreate heartCount", heartCount.toString())
        binding.heartCountTv.text = heartCount.toString()
        binding.moneyCountTv.text = SharedPreferencesManager.getIntValue(this, COIN, 0).toString()
        binding.expRatioTv.text = "    55%"

        if (score > 10) binding.resultLeftIv.setImageResource(R.drawable.ic_start_color)
        if (score > 20) binding.resultCenterIv.setImageResource(R.drawable.ic_start_color)
        if (score > 30) binding.resultRightIv.setImageResource(R.drawable.ic_start_color)
        if (score > 40)  {
            binding.resultLeftIv.setImageResource(R.drawable.ic_crawn_color)
            binding.resultCenterIv.setImageResource(R.drawable.ic_crawn_default)
            binding.resultRightIv.setImageResource(R.drawable.ic_crawn_default)
        }
        if (score > 50) binding.resultCenterIv.setImageResource(R.drawable.ic_crawn_color)
        if (score > 60) binding.resultRightIv.setImageResource(R.drawable.ic_crawn_color)

        //click listener
        binding.returnIv.setOnClickListener {
            storeData(maxScore)
            finish()
        }

        binding.restartIv.setOnClickListener {
            storeData(maxScore)
            finish()

            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        binding.nextIv.setOnClickListener {

        }
    }

    override fun onResume() {
        super.onResume()

        Thread() {
            for (i in 1..score) {
                mHandler.post{
                    binding.resultScoreTv.text = i.toString()
                }
                Thread.sleep(50)
            }
        }.start()

        mThread.start()
    }

    override fun onPause() {
        super.onPause()
        mThread.interrupt()
    }

    private fun storeData(max: Int) {
        SharedPreferencesManager.putIntValue(this, GameView.SCORE, max)
        val coin = SharedPreferencesManager.getIntValue(this, COIN)
        SharedPreferencesManager.putIntValue(this, COIN, coin + score * 10)
    }
}