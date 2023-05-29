package com.example.playlistmaker

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    private var onItemClicked: ((movie: Track) -> Unit)
) : RecyclerView.Adapter<TrackViewHolder> () {


    private val handler = Handler(Looper.getMainLooper())


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