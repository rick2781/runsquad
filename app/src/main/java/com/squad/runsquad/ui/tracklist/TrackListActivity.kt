package com.squad.runsquad.ui.tracklist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squad.runsquad.R
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrackListActivity : AppCompatActivity() {

    private val trackListViewModel: TrackListViewModel by viewModel()
    private val trackAdapter: TrackListAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_list)
    }
}