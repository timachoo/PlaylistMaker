package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSearch = findViewById<Button>(R.id.search_btn)

        /*val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажали на кнопку Поиск!", Toast.LENGTH_SHORT).show()
            }
        }
        btnSearch.setOnClickListener(imageClickListener)*/

        btnSearch.setOnClickListener{
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }

        val btnLibrary = findViewById<Button>(R.id.library_btn)

        /*
        btnLibrary.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку Медиатека!", Toast.LENGTH_SHORT).show()
        }
        */

        btnLibrary.setOnClickListener {
            val displayIntent = Intent(this, LibraryActivity::class.java)
            startActivity(displayIntent)
        }

        val btnSettings = findViewById<Button>(R.id.settings_btn)

        /*btnSettings.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку Настройки!", Toast.LENGTH_SHORT).show()
        }*/

        btnSettings.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}