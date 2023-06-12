package com.example.playlistmaker.presentstion.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.R
import com.example.playlistmaker.presentstion.library.LibraryActivity
import com.example.playlistmaker.presentstion.search.SearchActivity
import com.example.playlistmaker.presentstion.settings.SettingsActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSearch = findViewById<Button>(R.id.search_btn)

        btnSearch.setOnClickListener{
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }

        val btnLibrary = findViewById<Button>(R.id.library_btn)

        btnLibrary.setOnClickListener {
            val displayIntent = Intent(this, LibraryActivity::class.java)
            startActivity(displayIntent)
        }

        val btnSettings = findViewById<Button>(R.id.settings_btn)

        btnSettings.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }

    companion object {
        fun convertMillisToString(millis : Int) : String {
            return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
        }

    }
}