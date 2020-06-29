package com.squad.runsquad.ui.tracklist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.squad.runsquad.R
import com.squad.runsquad.ui.track.TrackActivity
import kotlinx.android.synthetic.main.activity_track_list.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrackListActivity : AppCompatActivity() {

    private val trackListViewModel: TrackListViewModel by viewModel()
    private val trackAdapter: TrackListAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_list)

        with(trackList) {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(this@TrackListActivity)
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

        with(trackListViewModel) {
            trackList.observe(this@TrackListActivity, Observer {
                trackAdapter.updateTrackList(it)
            })

            getTrackList()
        }

        fab.setOnClickListener { startActivity(Intent(this, TrackActivity::class.java)) }
    }
}