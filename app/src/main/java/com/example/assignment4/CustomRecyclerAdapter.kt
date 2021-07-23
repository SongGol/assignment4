package com.example.assignment4

import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment4.databinding.RecyclerviewItemBinding

class CustomRecyclerAdapter(var dataSet: ArrayList<Music>) : RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder>(){
    private lateinit var binding: RecyclerviewItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomRecyclerAdapter.ViewHolder {
        binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomRecyclerAdapter.ViewHolder, position: Int) {
        val item: Music = dataSet[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = dataSet.size

    inner class ViewHolder(private val binding: RecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.songPriceTv.setOnClickListener {
                Log.d("Custom Adapter", "button clicked")
            }
        }

        fun bind(data: Music) {
            binding.songTitleTv.text = data.title
            binding.songArtistTv.text = data.artist

            binding.songFavoriteIv.setImageResource(if (data.bHeart) R.drawable.ic_heart_selected else R.drawable.ic_heart_default)
            binding.songPriceTv.text = if (data.bPurchase) "시작" else data.price.toString()
            if (data.bPurchase) binding.songPriceCoinIv.setImageResource(0)
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