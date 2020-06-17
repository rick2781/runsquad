package com.squad.runsquad.ui.track

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squad.runsquad.util.aggregateValue
import com.squad.runsquad.util.round

class TrackViewModel : ViewModel() {

    val distanceTraveled = MutableLiveData<Float>()
    val timeElapsed = MutableLiveData<String>()

    private lateinit var lastLocation: Location

    fun updateLocation(location: Location) {

        if (!this::lastLocation.isInitialized) {
            lastLocation = location
            distanceTraveled.postValue(0F)
            return
        }

        updateDistanceTravelled(lastLocation.distanceTo(location))
        lastLocation = location
    }

    private fun updateDistanceTravelled(value: Float) = distanceTraveled.aggregateValue((value / 1000).round())
}