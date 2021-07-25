package com.example.assignment4

import android.content.Intent
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment4.databinding.ActivityMainBinding
import com.example.assignment4.databinding.RecyclerviewItemBinding

class CustomRecyclerAdapter(var dataSet: ArrayList<Music>, val mainBinding: ActivityMainBinding) : RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder>(){
    private lateinit var binding: RecyclerviewItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomRecyclerAdapter.ViewHolder {
        binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomRecyclerAdapter.ViewHolder, position: Int) {
        val item: Music = dataSet[position]
        holder.bind(item)
        Log.d("maxScore", item.maxScore.toString() + ", pos: " + position.toString())
    }

    override fun getItemCount(): Int = dataSet.size

    inner class ViewHolder(private val binding: RecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.songPriceTv.setOnClickListener {
                Log.d("Custom Adapter", "button clicked")
                val heartCount = SharedPreferencesManager.getIntValue(itemView.context, HEART, 10)
                var coin = SharedPreferencesManager.getIntValue(itemView.context, COIN)
                if (dataSet[adapterPosition].bPurchase) {
                    if (heartCount > 0) {
                        SharedPreferencesManager.putIntValue(itemView.context, GameView.POSITION, adapterPosition)
                        SharedPreferencesManager.putIntValue(itemView.context, GameView.SCORE, dataSet[adapterPosition].maxScore)
                        SharedPreferencesManager.putStrValue(itemView.context, GameView.NAME, dataSet[adapterPosition].title)
                        SharedPreferencesManager.putIntValue(itemView.context, HEART, heartCount - 1)

                        val intent = Intent(itemView.context, GameActivity::class.java)
                        intent.putExtra(MusicService.MKEY, dataSet[adapterPosition].title)
                        itemView.context.startActivity(intent)
                    } else {
                        Toast.makeText(itemView.context, "하트가 부족합니다", Toast.LENGTH_SHORT).show()
                    }
                } else if (!dataSet[adapterPosition].bPurchase && coin >= 1000){
                    coin -= 1000
                    dataSet[adapterPosition].bPurchase = true
                    notifyItemChanged(adapterPosition)
                    mainBinding.moneyCountTv.text = coin.toString()
                    SharedPreferencesManager.putIntValue(itemView.context, COIN, coin)
                } else {
                    Toast.makeText(itemView.context, "코인이 부족합니다", Toast.LENGTH_SHORT).show()
                }
            }
            //하트 선택
            binding.songFavoriteIv.setOnClickListener {
                dataSet[adapterPosition].bHeart = !dataSet[adapterPosition].bHeart
                notifyItemChanged(adapterPosition)
            }
        }

        fun bind(data: Music) {
            binding.songTitleTv.text = data.title
            binding.songArtistTv.text = data.artist
            binding.titleCoverIv.setImageResource(data.draw)

            binding.songFavoriteIv.setImageResource(if (data.bHeart) R.drawable.ic_heart_selected else R.drawable.ic_heart_default)
            if (data.bPurchase)  {
                binding.songPriceCoinIv.setImageResource(0)
                binding.songPriceTv.text = "시작"
            } else {
                binding.songPriceCoinIv.setImageResource(R.drawable.ic_money)
                binding.songPriceTv.text = "     X"+data.price.toString()
            }

            if (data.maxScore > 10) binding.songStarOneIv.setImageResource(R.drawable.ic_start_color)
            if (data.maxScore > 20) binding.songStarTwoIv.setImageResource(R.drawable.ic_start_color)
            if (data.maxScore > 30) binding.songStarThreeIv.setImageResource(R.drawable.ic_start_color)
            if (data.maxScore > 40)  {
                binding.songStarOneIv.setImageResource(R.drawable.ic_crawn_color)
                binding.songStarTwoIv.setImageResource(R.drawable.ic_crown_grey)
                binding.songStarThreeIv.setImageResource(R.drawable.ic_crown_grey)
            }
            if (data.maxScore > 50) binding.songStarTwoIv.setImageResource(R.drawable.ic_crawn_color)
            if (data.maxScore > 60) binding.songStarThreeIv.setImageResource(R.drawable.ic_crawn_color)
        }
    }
}

class RecyclerViewDecoration(private val divHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = divHeight
    }
}