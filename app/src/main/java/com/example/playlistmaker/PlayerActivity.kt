package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
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
    private lateinit var trackNameView: TextView
    private lateinit var artistNameView: TextView
    private lateinit var trackTimeView: TextView
    private lateinit var collectionNameView: TextView
    private lateinit var collectionNameCaptionView: TextView
    private lateinit var releaseDateView: TextView
    private lateinit var primaryGenreNameView: TextView
    private lateinit var countryView: TextView
    private lateinit var trackImageView: ImageView
    private lateinit var btnPlay: ImageButton
    private lateinit var playTimeView: TextView
    private lateinit var track:Track

    private var playerState = PlayerStatus.Default
    private var mediaPlayer = MediaPlayer()
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var setTextRunnable : Runnable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val btnBack = findViewById<View>(R.id.back_btn)

        btnBack.setOnClickListener {
            finish()
        }

        val json = intent.extras?.getString(TRACK_KEY)

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
            btnPlay = findViewById<ImageButton>(R.id.play)
            playTimeView = findViewById<TextView>(R.id.playTime)

            Glide.with(trackImageView).load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
                .placeholder(R.drawable.placeholder_big)
                .centerCrop()
                .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.big_image_radius)))
                .into(trackImageView)

            trackNameView.setText(track.trackName)
            artistNameView.setText(track.artistName)
            trackTimeView.setText(MainActivity.convertMillisToString(track.trackTimeMillis))
            if (!track.collectionName.isEmpty()){
                collectionNameView.visibility = View.VISIBLE
                collectionNameCaptionView.visibility = View.VISIBLE
                collectionNameView.setText(track.collectionName)
            } else {
                collectionNameView.visibility = View.GONE
                collectionNameCaptionView.visibility = View.GONE
            }
            if (!track.releaseDate.isEmpty()) {
                var date = LocalDate.parse(track.releaseDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ"))
                releaseDateView.setText(date.year.toString())
            }
            primaryGenreNameView.setText(track.primaryGenreName)
            countryView.setText(track.country)

            playTimeView.setText(R.string.player_start_position)

            btnPlay.setOnClickListener {
                playbackControl()
            }
            if (!track.previewUrl.isEmpty()) {
                preparePlayer(track.previewUrl)
            }

            setTextRunnable = Runnable {
                    playTimeView.setText(MainActivity.convertMillisToString(mediaPlayer.currentPosition))
                    handler.postDelayed(setTextRunnable, DELAY_MILLIS)
            }

        }

    }

    private fun preparePlayer(url : String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerStatus.Prepared
        }
        mediaPlayer.setOnCompletionListener {
            btnPlay.setImageResource(R.drawable.play)
            playerState = PlayerStatus.Prepared
            playTimeView.setText(R.string.player_start_position)
            if(setTextRunnable != null) {
                handler.removeCallbacks(setTextRunnable)
            }
        }
    }

    private fun startPlayer() {
        if (!track.previewUrl.isEmpty()) {
            mediaPlayer.start()
            btnPlay.setImageResource(R.drawable.pause)
            playerState = PlayerStatus.Playing
            handler.post(setTextRunnable)
        }
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        btnPlay.setImageResource(R.drawable.play)
        playerState = PlayerStatus.Paused
    }

    private fun playbackControl() {
        when(playerState) {
            PlayerStatus.Playing -> {
                pausePlayer()
            }
            PlayerStatus.Prepared, PlayerStatus.Paused -> {
                startPlayer()
            }
            else -> {
                pausePlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
        if(this::setTextRunnable.isInitialized) {
            handler.removeCallbacks(setTextRunnable)
        }
    }

    companion object{
        const val TRACK_KEY = "track_key"
        private const val DELAY_MILLIS = 1000L
    }
}