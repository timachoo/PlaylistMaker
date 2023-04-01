package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

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
        if (track.trackName.length > 23)
            trackName.text = track.trackName.substring(1,20) + "..."
        else
            trackName.text = track.trackName

        if (track.artistName.length > 23)
            artistName.text = track.artistName.substring(1,20) + "..."
        else
            artistName.text = track.artistName

        artistName.text = track.artistName
        trackTime.text = track.trackTime
        Glide.with(itemView).load(track.artworkUrl100)
                            .placeholder(R.drawable.placeholder)
                            .centerCrop()
                            .transform(RoundedCorners(2))
                            .into(artworkUrl100)
    }
}