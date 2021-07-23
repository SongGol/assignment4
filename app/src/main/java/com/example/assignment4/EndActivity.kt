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

        val name = intent.getStringExtra(GameView.NAME)
        val score = intent.getIntExtra(GameView.SCORE, -1)
        val maxScore = SharedPreferencesManager.getIntValue(this, name!!)

        binding.songTitleTv.text = name
        binding.resultScoreTv.text = score.toString()
        binding.maxScoreTv.text = String.format(resources.getString(R.string.max_score_tv, maxScore))
        binding.resultExpTv.text = String.format(resources.getString(R.string.result_exp_tv, 0))
        binding.resultCoinTv.text = String.format(resources.getString(R.string.result_coin_tv, 0))
    }
}