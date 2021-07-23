package com.example.assignment4

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment4.databinding.ActivityEndBinding

class EndActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEndBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val name = SharedPreferencesManager.getStrValue(this, GameView.NAME)
        val score = GameActivity.score
        var maxScore = SharedPreferencesManager.getIntValue(this, GameView.SCORE)
        if (maxScore < score) maxScore = score

        binding.songTitleTv.text = name
        binding.resultScoreTv.text = score.toString()
        binding.maxScoreTv.text = String.format(resources.getString(R.string.max_score_tv, maxScore))
        binding.resultExpTv.text = String.format(resources.getString(R.string.result_exp_tv, 0))
        binding.resultCoinTv.text = String.format(resources.getString(R.string.result_coin_tv, 0))

        if (score > 100) binding.resultLeftIv.setImageResource(R.drawable.ic_start_color)
        if (score > 200) binding.resultCenterIv.setImageResource(R.drawable.ic_start_color)
        if (score > 300) binding.resultRightIv.setImageResource(R.drawable.ic_start_color)
        if (score > 400)  {
            binding.resultLeftIv.setImageResource(R.drawable.ic_crawn_color)
            binding.resultCenterIv.setImageResource(R.drawable.ic_crawn_default)
            binding.resultRightIv.setImageResource(R.drawable.ic_crawn_default)
        }
        if (score > 500) binding.resultCenterIv.setImageResource(R.drawable.ic_crawn_color)
        if (score > 600) binding.resultRightIv.setImageResource(R.drawable.ic_crawn_color)

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

    override fun onPause() {
        super.onPause()

    }

    private fun storeData(max: Int) {
        SharedPreferencesManager.putIntValue(this, GameView.SCORE, max)
    }
}