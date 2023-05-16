package com.example.playlistmaker

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class TrackAdapter: RecyclerView.Adapter<TrackViewHolder> () {

    var trackList = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            SearchHistory.add(trackList[position])
            val displayIntent = Intent(it.context, PlayerActivity::class.java)
            displayIntent.putExtra(PlayerActivity.TRACK_KEY, Gson().toJson(trackList[position]))
            it.context.startActivity(displayIntent)
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}