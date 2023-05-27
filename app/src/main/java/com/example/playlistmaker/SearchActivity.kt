package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(iTunesAPI::class.java)
    private val trackList = ArrayList<Track>()
    private lateinit var adapter: TrackAdapter
    private var searchHistoryList = ArrayList<Track>()

    private lateinit var inputEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var statusLayout: LinearLayout
    private lateinit var statusImage: ImageView
    private lateinit var statusCaption: TextView
    private lateinit var statusAddText: TextView
    private lateinit var btnReload: Button
    private lateinit var historySearchText: TextView
    private lateinit var btnClearHistory: Button
    private lateinit var progresBar: ProgressBar

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { iTunesSearch() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        statusLayout = findViewById<LinearLayout>(R.id.status)
        statusImage = findViewById<ImageView>(R.id.status_img)
        statusCaption = findViewById<TextView>(R.id.status_caption)
        statusAddText = findViewById<TextView>(R.id.status_add_text)
        btnReload = findViewById<Button>(R.id.reload_btn)
        historySearchText = findViewById<TextView>(R.id.history_search_text)
        btnClearHistory = findViewById<Button>(R.id.clear_history_btn)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        progresBar = findViewById<ProgressBar>(R.id.progressBar)

        btnReload.setOnClickListener {
            iTunesSearch()
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
            searchHistoryList = SearchHistory.fill()
            viewResult(
                if (searchHistoryList.size > 0)
                    TrackSearchStatus.ShowHistory
                else TrackSearchStatus.Empty
            )
        }

        btnClearHistory.setOnClickListener {
            searchHistoryList.clear()
            SearchHistory.clear()
            viewResult(TrackSearchStatus.Empty)
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

        searchHistoryList = SearchHistory.fill()
        adapter = TrackAdapter(){
                it: Track ->
                    SearchHistory.add(it)
                    val displayIntent = Intent(this, PlayerActivity::class.java)
                    displayIntent.putExtra(PlayerActivity.TRACK_KEY, Gson().toJson(it))
                    startActivity(displayIntent)
        }
        recyclerView.adapter = adapter
        viewResult(
            if (searchHistoryList.size > 0)
                TrackSearchStatus.ShowHistory
            else TrackSearchStatus.Empty
        )

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                iTunesSearch()
                true
            }
            false
        }

        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchDebounce()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

    }
    private fun clearButtonVisibility(s: CharSequence?): Int {
        if (s.isNullOrEmpty()) {
            trackList.clear()
            viewResult(TrackSearchStatus.Success)
            return View.GONE
        } else {
            return View.VISIBLE
        }
    }

    private fun viewResult(status : TrackSearchStatus) {
        when (status) {
            TrackSearchStatus.Empty -> {
                statusLayout.visibility = View.GONE
                recyclerView.visibility = View.GONE
                historySearchText.visibility = View.GONE
                btnClearHistory.visibility = View.GONE
                progresBar.visibility = View.GONE
            }
            TrackSearchStatus.Success -> {
                statusLayout.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                historySearchText.visibility = View.GONE
                btnClearHistory.visibility = View.GONE
                adapter.trackList = trackList
                adapter.notifyDataSetChanged()
                progresBar.visibility = View.GONE
            }
            TrackSearchStatus.NoDataFound -> {
                statusLayout.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                statusImage.setImageResource(R.drawable.nodatafound)
                statusAddText.visibility = View.GONE
                btnReload.visibility = View.GONE
                statusCaption.setText(R.string.no_data_found)
                historySearchText.visibility = View.GONE
                btnClearHistory.visibility = View.GONE
                progresBar.visibility = View.GONE
            }
            TrackSearchStatus.ConnectionError -> {
                statusLayout.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                statusImage.setImageResource(R.drawable.connect_error)
                statusAddText.visibility = View.VISIBLE
                btnReload.visibility = View.VISIBLE
                statusCaption.setText(R.string.connect_err)
                historySearchText.visibility = View.GONE
                btnClearHistory.visibility = View.GONE
                progresBar.visibility = View.GONE
            }
            TrackSearchStatus.ShowHistory -> {
                statusLayout.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                historySearchText.visibility = View.VISIBLE
                btnClearHistory.visibility = View.VISIBLE
                progresBar.visibility = View.GONE
                adapter.trackList = searchHistoryList
                adapter.notifyDataSetChanged()
            }
            TrackSearchStatus.Progress -> {
                statusLayout.visibility = View.GONE
                recyclerView.visibility = View.GONE
                historySearchText.visibility = View.GONE
                btnClearHistory.visibility = View.GONE
                progresBar.visibility = View.VISIBLE
            }
        }
    }

    private fun iTunesSearch(){
        if (inputEditText.text.isNotEmpty()){

            viewResult(TrackSearchStatus.Progress)

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
                            viewResult(TrackSearchStatus.NoDataFound)
                        } else {
                            viewResult(TrackSearchStatus.Success)
                        }
                    } else {
                        viewResult(TrackSearchStatus.ConnectionError)
                    }
                }

                override fun onFailure(call: Call<TrackResponce>, t: Throwable) {
                    viewResult(TrackSearchStatus.ConnectionError)
                }

            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, inputEditText.text.toString())
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
}