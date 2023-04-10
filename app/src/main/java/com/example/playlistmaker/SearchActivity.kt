package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(iTunesAPI::class.java)
    private val trackList = ArrayList<Track>()
    private val adapter = TrackAdapter()
    private var tempText = ""

    private lateinit var inputEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var noDataFound: LinearLayout
    private lateinit var connectError: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        noDataFound = findViewById<LinearLayout>(R.id.nodatafound)
        connectError = findViewById<LinearLayout>(R.id.connect_error)

        val btnReload = findViewById<Button>(R.id.reload_btn)

        btnReload.setOnClickListener {
            if (tempText.isNotEmpty()) {
                inputEditText.setText(tempText)
            }
        }

        inputEditText = findViewById<EditText>(R.id.search_edit_text)
        if (savedInstanceState != null) {
            inputEditText.setText(savedInstanceState.getString(SEARCH_TEXT,""))
        }

        val btnBack = findViewById<View>(R.id.search_back_btn)

        btnBack.setOnClickListener {
            finish()
        }

        val clearButton = findViewById<ImageView>(R.id.removeBtn)

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter.trackList = trackList
        recyclerView.adapter = adapter

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()){
                    iTunesService.search(inputEditText.text.toString()).enqueue(object :
                        Callback<TrackResponce> {
                        override fun onResponse(call: Call<TrackResponce>,
                                                response: Response<TrackResponce>
                        ) {
                            if (response.code() == 200) {
                                trackList.clear()
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    trackList.addAll(response.body()?.results!!)
                                    adapter.notifyDataSetChanged()
                                }
                                if (trackList.isEmpty()) {
                                    noDataFoundVisible()
                                } else {
                                    recyclerVisible()
                                }
                            } else {
                                errorVisible()
                            }
                        }

                        override fun onFailure(call: Call<TrackResponce>, t: Throwable) {
                            errorVisible()
                        }

                    })
                }
            }
            false
        }
    }
    private fun clearButtonVisibility(s: CharSequence?): Int {
        if (s.isNullOrEmpty()) {
            trackList.clear()
            adapter.notifyDataSetChanged()
            recyclerVisible()
            return View.GONE
        } else {
            return View.VISIBLE
        }
    }
    private fun recyclerVisible(){
        connectError.visibility = View.GONE
        noDataFound.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun noDataFoundVisible(){
        connectError.visibility = View.GONE
        noDataFound.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun errorVisible(){
        connectError.visibility = View.VISIBLE
        noDataFound.visibility = View.GONE
        recyclerView.visibility = View.GONE
        tempText = inputEditText.text.toString()
        inputEditText.text.clear()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, inputEditText.text.toString())
    }
}