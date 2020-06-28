package com.squad.runsquad.ui.tracklist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squad.runsquad.data.model.Track
import com.squad.runsquad.repository.TrackRepository

class TrackListViewModel(private val trackRepository: TrackRepository): ViewModel() {

    val trackList = MutableLiveData<ArrayList<Track>>()

    fun getTrackList() = trackRepository.getAllTracks { trackList.postValue(it) }
}