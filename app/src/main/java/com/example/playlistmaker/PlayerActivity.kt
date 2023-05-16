package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity  : AppCompatActivity() {
    companion object{
        const val TRACK_KEY = "track_key"
    }

    private lateinit var trackNameView: TextView
    private lateinit var artistNameView: TextView
    private lateinit var trackTimeView: TextView
    private lateinit var collectionNameView: TextView
    private lateinit var collectionNameCaptionView: TextView
    private lateinit var releaseDateView: TextView
    private lateinit var primaryGenreNameView: TextView
    private lateinit var countryView: TextView
    private lateinit var trackImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val btnBack = findViewById<View>(R.id.back_btn)

        btnBack.setOnClickListener {
            finish()
        }

        val json = intent.extras?.getString(TRACK_KEY)
        val track:Track

        if (!json.isNullOrEmpty()) {
            track = Gson().fromJson(json, Track::class.java)

            trackNameView = findViewById<TextView>(R.id.trackName)
            artistNameView = findViewById<TextView>(R.id.artistName)
            trackTimeView = findViewById<TextView>(R.id.trackTime)
            collectionNameView = findViewById<TextView>(R.id.collectionName)
            collectionNameCaptionView = findViewById<TextView>(R.id.collectionNameCaption)
            releaseDateView = findViewById<TextView>(R.id.releaseDate)
            primaryGenreNameView = findViewById<TextView>(R.id.primaryGenreName)
            countryView = findViewById<TextView>(R.id.country)
            trackImageView = findViewById<ImageView>(R.id.trackImage)

            Glide.with(trackImageView).load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
                .placeholder(R.drawable.placeholder_big)
                .centerCrop()
                .transform(RoundedCorners(2))
                .into(trackImageView)

            trackNameView.setText(track.trackName)
            artistNameView.setText(track.artistName)
            trackTimeView.setText(SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis))
            if (!track.collectionName.isNullOrEmpty()){
                collectionNameView.visibility = View.VISIBLE
                collectionNameCaptionView.visibility = View.VISIBLE
                collectionNameView.setText(track.collectionName)
            } else {
                collectionNameView.visibility = View.GONE
                collectionNameCaptionView.visibility = View.GONE
            }
            if (!track.releaseDate.isNullOrEmpty()) {
                var date = LocalDate.parse(track.releaseDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ"))
                releaseDateView.setText(date.year.toString())
            }
            primaryGenreNameView.setText(track.primaryGenreName)
            countryView.setText(track.country)
        }

    }
}