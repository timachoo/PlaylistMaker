package com.example.playlistmaker.presentstion.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class TrackAdapter(
    private var onItemClicked: ((movie: Track) -> Unit)
) : RecyclerView.Adapter<TrackViewHolder> () {
    var trackList = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            onItemClicked(trackList[position])
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}