package com.squad.runsquad.ui.track

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squad.runsquad.data.model.TrackState
import com.squad.runsquad.util.aggregateValue
import com.squad.runsquad.util.round
import java.util.concurrent.TimeUnit

class TrackViewModel : ViewModel() {

    /**
     * Distance travelled in km - e.g. 1.3 = 1km 300 meters
     */
    val distanceTraveled = MutableLiveData<Float>()

    /**
     * Time elapsed in milliseconds
     */
    val timeElapsed = MutableLiveData<Long>()

    /**
     * average pace in "time" - e.g. 2.4 = 2 minutes 40 seconds
     */
    val averagePace = MutableLiveData<Float>()

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

        calculateAveragePace(timeElapsed.value!!, distanceTraveled.value!!)
    }

    private fun calculateAveragePace(timeMillis: Long, distance: Float) {

        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMillis)
        val seconds = (timeMillis / 1000) % 60

        val timeDecimal = "$minutes.$seconds".toFloat()

        averagePace.postValue(timeDecimal / distance)
    }

    private fun updateDistanceTravelled(value: Float) {
        distanceTraveled.aggregateValue((value / 1000))
    }

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