package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnSettingsTheme = findViewById<Switch>(R.id.settings_theme_switch)

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            btnSettingsTheme.setChecked(true);
        }

        val btnSettingsBack = findViewById<View>(R.id.settings_back_btn)

        btnSettingsBack.setOnClickListener {
            finish()
        }

        btnSettingsTheme.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val btnShare = findViewById<View>(R.id.settings_share)

        btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.url_course))
            startActivity(shareIntent)
        }

        val btnSupport = findViewById<View>(R.id.settings_support)

        btnSupport.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(resources.getString(R.string.developer_email)))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.default_theme))
            shareIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.default_message))
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(shareIntent)
            }
        }

        val btnAgreement = findViewById<View>(R.id.settings_agreement)

        btnAgreement.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_VIEW)
            shareIntent.data = Uri.parse(resources.getString(R.string.url_agreement))
            startActivity(shareIntent)
        }
    }
}
