package com.example.assignment4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment4.databinding.ActivityMainBinding

const val HEART = "heart"
const val COIN = "coin"
const val EXP = "exp"
const val MUSIC_DATA = "musicData"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var customAdapter: CustomRecyclerAdapter
    private lateinit var musicArray: ArrayList<Music>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        musicArray = SharedPreferencesManager.getObject(this, MUSIC_DATA, initialDataSet())
        customAdapter = CustomRecyclerAdapter(musicArray, binding)
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.mainRecyclerView.adapter = customAdapter
        binding.mainRecyclerView.addItemDecoration(RecyclerViewDecoration(10))
        //상단 숫자 설정
        binding.heartCountTv.text = SharedPreferencesManager.getIntValue(this, HEART, 10).toString()
        binding.moneyCountTv.text = SharedPreferencesManager.getIntValue(this, COIN, 0).toString()
        binding.expRatioTv.text = "55%"

        binding.mainBonusCoinIv.setOnClickListener {
            var coin: Int = SharedPreferencesManager.getIntValue(this, COIN, 0)
            coin += 150
            binding.moneyCountTv.text = coin.toString()
            SharedPreferencesManager.putIntValue(this, COIN, coin)
        }

        binding.heartAddIv.setOnClickListener {
            var heart = SharedPreferencesManager.getIntValue(this, HEART, 10)
            heart += 1
            binding.heartCountTv.text = heart.toString()
            SharedPreferencesManager.putIntValue(this, HEART, heart)

            val intent = Intent(this, MusicService::class.java)
            startService(intent)
        }

        binding.moneyAddTv.setOnClickListener {
            var coin: Int = SharedPreferencesManager.getIntValue(this, COIN, 0)
            coin += 100
            binding.moneyCountTv.text = coin.toString()
            SharedPreferencesManager.putIntValue(this, COIN, coin)

            val intent = Intent(this, MusicService::class.java)
            stopService(intent)
        }
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("MainActivity", "onRestart()")
        val pos = SharedPreferencesManager.getIntValue(this, GameView.POSITION)
        val mScore = SharedPreferencesManager.getIntValue(this, GameView.SCORE)
        val heartCount = SharedPreferencesManager.getIntValue(this, HEART)
        binding.heartCountTv.text = heartCount.toString()
        if (musicArray[pos].maxScore < mScore) {
            Log.d("MainActivity", "onRestart() maxScore changed")
            musicArray[pos].maxScore = mScore
            customAdapter.notifyItemChanged(pos)
        }
    }

    private fun initialDataSet() : ArrayList<Music> {
        val default = ArrayList<Music>()

        default.add(Music("캐논", "파헬벨", bPurchase = true))
        default.add(Music("징글벨", "제임스 로즈 필 테", draw = R.drawable.ic_jingle_bells))
        default.add(Music("반짝반짝 작은 별", "ABBA", draw = R.drawable.ic_blinkle_star))
        default.add(Music("Happy New Year", "영국 민속", draw = R.drawable.ic_happy_new_year))
        default.add(Music("The Spectre", "Alan Walker", draw = R.drawable.ic_the_spectre))
        default.add(Music("Alone", "Alan Walker", draw = R.drawable.ic_alone))
        default.add(Music("Faded", "Alan Walker", draw = R.drawable.ic_faded))
        default.add(Music("Eye Of The Tiger", "SURVIVOR", draw = R.drawable.ic_two_tigers))

        return default
    }

    override fun onStop() {
        super.onStop()

        SharedPreferencesManager.putObject(this, MUSIC_DATA, musicArray)
    }
}