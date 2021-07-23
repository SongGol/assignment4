package com.example.assignment4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    override fun onResume() {
        super.onResume()

        binding.mainScore.text = SharedPreferencesManager.getIntValue(this, SharedPreferencesManager.SCORE).toString()
    }

    private fun initialDataSet() : ArrayList<Music> {
        val default = ArrayList<Music>()

        default.add(Music("캐논", "파헬벨", bPurchase = true, bHeart = true))
        default.add(Music("캐논", "파헬벨", price = 0))
        default.add(Music("캐논", "파헬벨", price = 0))
        default.add(Music("캐논", "파헬벨", price = 0))
        default.add(Music("캐논", "파헬벨", price = 0))
        default.add(Music("캐논", "파헬벨", price = 0))
        default.add(Music("캐논", "파헬벨", price = 0))
        default.add(Music("캐논", "파헬벨", price = 0))
        default.add(Music("캐논", "파헬벨", price = 0))
        default.add(Music("캐논", "파헬벨", price = 0))
        default.add(Music("캐논", "파헬벨", price = 0))

        return default
    }

    override fun onStop() {
        super.onStop()

        SharedPreferencesManager.putObject(this, MUSIC_DATA, musicArray)
    }
}