package com.example.playlistmaker.presentstion.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentstion.main.MainActivity


class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView
    private val artistName: TextView
    private val trackTime: TextView
    private val artworkUrl100: ImageView

    init {
        trackName = itemView.findViewById(R.id.trackName)
        artistName = itemView.findViewById(R.id.artistName)
        trackTime = itemView.findViewById(R.id.trackTime)
        artworkUrl100 = itemView.findViewById(R.id.trackImage)
    }

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        artistName.text = track.artistName
        trackTime.text = MainActivity.convertMillisToString(track.trackTimeMillis)
        Glide.with(itemView).load(track.artworkUrl100)
                            .placeholder(R.drawable.placeholder)
                            .centerCrop()
                            .transform(RoundedCorners(2))
                            .into(artworkUrl100)
    }
}