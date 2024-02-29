package com.example.musicplayerservice.presentation.adapter

import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayerservice.databinding.ItemMusicBinding
import com.example.musicplayerservice.utils.getMusicDataByPosition

class MusicAdapter : RecyclerView.Adapter<MusicAdapter.MyCursorViewHolder>() {

    var cursor: Cursor? = null
    private var selectMusicPositionListener: ((Int) -> Unit)? = null

    inner class MyCursorViewHolder(private val binding: ItemMusicBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                selectMusicPositionListener?.invoke(absoluteAdapterPosition)
            }
        }

        fun bind(pos: Int) {
            cursor?.getMusicDataByPosition(absoluteAdapterPosition)?.let {
                binding.songTitle.text = it.title
                binding.songArtist.text = it.artist
                binding.tvOrder.text = "${pos.plus(1)}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyCursorViewHolder(ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MyCursorViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cursor?.count ?: 0

    fun setSelectMusicPositionListener(block: (Int) -> Unit) {
        selectMusicPositionListener = block
    }
}