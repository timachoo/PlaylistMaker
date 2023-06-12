package com.example.playlistmaker.presentstion.settings

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnSettingsBack = findViewById<View>(R.id.settings_back_btn)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val btnShare = findViewById<View>(R.id.settings_share)
        val btnSupport = findViewById<View>(R.id.settings_support)
        val btnAgreement = findViewById<View>(R.id.settings_agreement)

        btnSettingsBack.setOnClickListener {
            finish()
        }

        if ((applicationContext as App).darkTheme) {
            themeSwitcher.setChecked(true);
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.url_course))
            startActivity(shareIntent)
        }

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

        btnAgreement.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_VIEW)
            shareIntent.data = Uri.parse(resources.getString(R.string.url_agreement))
            startActivity(shareIntent)
        }
    }
}
