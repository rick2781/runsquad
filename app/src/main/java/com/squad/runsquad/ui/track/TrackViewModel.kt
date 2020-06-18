package com.squad.runsquad.ui.track

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squad.runsquad.data.model.TrackState
import com.squad.runsquad.util.aggregateValue
import com.squad.runsquad.util.round

class TrackViewModel : ViewModel() {

    val distanceTraveled = MutableLiveData<Float>()
    val timeElapsed = MutableLiveData<Long>()

    val isRunning = MutableLiveData<TrackState>()

    private lateinit var lastLocation: Location

    init {
        isRunning.postValue(TrackState.NOT_STARTED)
    }

    /**
     * First entry point to update UI. Here we are receiving location from activity and delegating
     * business logic to other functions as appropriate and updating the views.
     */
    fun updateLocation(location: Location) {

        //TODO - simplify this updating distance logic
        if (!this::lastLocation.isInitialized) {
            lastLocation = location
            distanceTraveled.postValue(0F)
            return
        }

        updateDistanceTravelled(lastLocation.distanceTo(location))
        lastLocation = location
    }

    private fun updateDistanceTravelled(value: Float) = distanceTraveled.aggregateValue((value / 1000).round())

    fun start() {
        isRunning.postValue(TrackState.ACTIVE)
    }

    fun pause() {
        isRunning.postValue(TrackState.PAUSED)
    }

    fun resume() {
        isRunning.postValue(TrackState.ACTIVE)
    }

    fun stop() {
        isRunning.postValue(TrackState.STOPPED)
    }
}