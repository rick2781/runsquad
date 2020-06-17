package com.squad.runsquad.ui.track

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squad.runsquad.util.aggregateValue

class TrackViewModel : ViewModel() {

    val distanceTraveled = MutableLiveData<Float>()
    val timeElapsed = MutableLiveData<String>()

    private lateinit var lastLocation: Location

    fun updateLocation(location: Location) {

        if (!this::lastLocation.isInitialized) {
            lastLocation = location
            return
        }

        distanceTraveled.aggregateValue(lastLocation.distanceTo(location))

    }
}