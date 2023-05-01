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
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnSettingsBack = findViewById<View>(R.id.settings_back_btn)

        btnSettingsBack.setOnClickListener {
            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        if ((applicationContext as App).darkTheme) {
            themeSwitcher.setChecked(true);
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
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
