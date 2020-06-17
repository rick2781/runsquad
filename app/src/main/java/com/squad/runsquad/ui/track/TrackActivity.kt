package com.squad.runsquad.ui.track

import android.os.Bundle
import android.os.SystemClock
import android.widget.Chronometer
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.squad.runsquad.R
import kotlinx.android.synthetic.main.activity_main.*


class TrackActivity : AppCompatActivity() {

    private val locationRequest = LocationRequest()
    private lateinit var locationCallback: LocationCallback

    private val trackViewModel by viewModels<TrackViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupLocationRequest()
        setupChronometer()
    }

    private fun setupLocationRequest() {

        with(locationRequest) {
            interval = UPDATE_INTERVAL
            fastestInterval = FASTEST_UPDATE_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

//                mCurrentLocation = locationResult.lastLocation
//                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
//                updateLocationUI()
            }
        }
    }

    private fun setupChronometer() {
        chronometer.onChronometerTickListener = Chronometer.OnChronometerTickListener {
            val time: Long = SystemClock.elapsedRealtime() - it.base
            val h = (time / 3600000).toInt()
            val m = (time - h * 3600000).toInt() / 60000
            val s = (time - h * 3600000 - m * 60000).toInt() / 1000
            it.text = String.format("%02d:%02d:%02d", h, m, s)
        }
    }

    private companion object {

        /**
         * Interval in milliseconds. It's inexact. Can happen earlier or later than that.
         */
        const val UPDATE_INTERVAL = 10000L

        /**
         * Fastest interval in milliseconds. It's exact. It will never happen more often than this value
         */
        const val FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2
    }
}