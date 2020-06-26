package com.squad.runsquad.ui.track

import android.location.Location
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squad.runsquad.data.model.Track
import com.squad.runsquad.data.model.TrackState
import com.squad.runsquad.repository.TrackRepository
import com.squad.runsquad.util.aggregateValue
import com.squad.runsquad.util.round
import java.util.concurrent.TimeUnit

class TrackViewModel(private val trackRepository: TrackRepository) : ViewModel() {

    /**
     * Distance travelled in km - e.g. 1.3 = 1km 300 meters
     */
    val distanceTravelled = MutableLiveData<Float>()

    /**
     * Time elapsed in milliseconds
     */
    val timeElapsed = MutableLiveData<Long>()

    /**
     * average pace in "time" - e.g. 2.4 = 2 minutes 40 seconds
     */
    val averagePace = MutableLiveData<Float>()

    val isRunning = MutableLiveData<TrackState>()

    val distanceTimeMediator = MediatorLiveData<Number>()

    private var lastLocation: Location? = null

    init {
        setupDistanceTimeMediatorSources()
    }

    /**
     * First entry point to update UI. Here we are receiving location from activity and delegating
     * business logic to other functions as appropriate and updating the views.
     */
    fun updateLocation(location: Location) {

        //TODO - simplify this updating distance logic
        if (lastLocation == null) {
            lastLocation = location
            if (distanceTravelled.value == null) distanceTravelled.postValue(0F)
            return
        }

        updateDistanceTravelled(lastLocation!!.distanceTo(location))
        lastLocation = location
    }

    private fun setupDistanceTimeMediatorSources() {
        with(distanceTimeMediator) {
            addSource(distanceTravelled) {
                distanceTimeMediator.value = null
            }

            addSource(timeElapsed) {
                distanceTimeMediator.value = null
            }
        }
    }

    private fun calculateAveragePace(timeMillis: Long, distance: Float) {

        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMillis)
        val seconds = (timeMillis / 1000) % 60

        val timeDecimal = "$minutes.$seconds".toFloat()
        val averagePaceValue = timeDecimal / distance

        if (averagePaceValue.isFinite()) averagePace.postValue(averagePaceValue.round())
    }

    private fun saveTrack() {
        if (distanceTravelled.value != null && timeElapsed.value != null && averagePace.value != null)
            trackRepository.saveTrack(Track().apply {
                distanceTravelled = this@TrackViewModel.distanceTravelled.value!!
                timeElapsed = this@TrackViewModel.timeElapsed.value!!
                averagePace = this@TrackViewModel.averagePace.value!!
            })
    }

    private fun resetTrackValues() {
        distanceTravelled.value = 0F
        timeElapsed.value = 0L
        averagePace.value = 0F
    }

    private fun updateDistanceTravelled(value: Float) {
        distanceTravelled.aggregateValue((value / 1000))
    }

    fun updatePace() {
        if (distanceTravelled.value != null && timeElapsed.value != null)
            calculateAveragePace(timeElapsed.value!!, distanceTravelled.value!!)
    }

    fun start() {
        isRunning.postValue(TrackState.ACTIVE)
    }

    fun pause() {
        isRunning.postValue(TrackState.INACTIVE)
        lastLocation = null
    }

    fun stop() {
        isRunning.postValue(TrackState.INACTIVE)
        saveTrack()
        resetTrackValues()
    }
}