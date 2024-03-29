package com.example.playlistmaker.data

import com.example.playlistmaker.App
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory {
    companion object {
        const val HISTORY_TRACK_LIST = "key_for_history_track_list"
        const val MAX_HISTORY_SIZE = 10

        private fun wright(historyTrackList: ArrayList<Track>) {
            val json = Gson().toJson(historyTrackList)
            App.applicationPrefs.edit()
                .putString(HISTORY_TRACK_LIST, json)
                .apply()
        }

        fun fill():ArrayList<Track> {
            var historyTrackList = ArrayList<Track>()
            val json = App.applicationPrefs.getString(HISTORY_TRACK_LIST, null)
            if (!json.isNullOrEmpty()) {
                val sType = object : TypeToken<ArrayList<Track>>() {}.type
                historyTrackList = Gson().fromJson<ArrayList<Track>>(json, sType)
            }
            return historyTrackList
        }

        fun add(track: Track) {
            var historyTrackList = fill()

            if (historyTrackList.contains(track)) {
                historyTrackList.remove(track)
            }

            if (historyTrackList.size >= MAX_HISTORY_SIZE) {
                historyTrackList.removeAt(historyTrackList.size - 1)
            }

            historyTrackList.add(0, track)

            wright(historyTrackList)
        }

        fun clear(){
            wright(ArrayList<Track>())
        }
    }
}