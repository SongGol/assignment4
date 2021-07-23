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
        customAdapter = CustomRecyclerAdapter(musicArray)
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.mainRecyclerView.adapter = customAdapter
        binding.mainRecyclerView.addItemDecoration(RecyclerViewDecoration(10))

        binding.tmpBtn.setOnClickListener{
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("MainActivity", "onRestart()")
        val pos = SharedPreferencesManager.getIntValue(this, GameView.POSITION)
        val mScore = SharedPreferencesManager.getIntValue(this, GameView.SCORE)
        if (musicArray[pos].maxScore < mScore) {
            musicArray[pos].maxScore = mScore
            customAdapter.notifyItemChanged(pos)
        }
    }

    override fun onResume() {
        super.onResume()

        binding.mainScore.text = SharedPreferencesManager.getIntValue(this, SharedPreferencesManager.SCORE).toString()
    }

    private fun initialDataSet() : ArrayList<Music> {
        val default = ArrayList<Music>()

        default.add(Music("캐논", "파헬벨", bPurchase = true, bHeart = true))
        default.add(Music("징글벨", "제임스 로즈 필 테", draw = R.drawable.ic_jingle_bells))
        default.add(Music("반짝반짝 작은 별", "영국 민속", draw = R.drawable.ic_blinkle_star))
        default.add(Music("Happy New Year", "영국 민속", draw = R.drawable.ic_happy_new_year))
        default.add(Music("The Spectre", "Alan Walker", draw = R.drawable.ic_the_spectre))
        default.add(Music("Alone", "Alan Walker", draw = R.drawable.ic_alone))
        default.add(Music("Faded", "Alan Walker", draw = R.drawable.ic_faded))
        default.add(Music("호랑이 두 마리", "프랑스 발라드", draw = R.drawable.ic_two_tigers))

        return default
    }

    override fun onStop() {
        super.onStop()

        SharedPreferencesManager.putObject(this, MUSIC_DATA, musicArray)
    }
}