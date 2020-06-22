package com.squad.runsquad.ui.track

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.widget.Chronometer
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.squad.runsquad.R
import com.squad.runsquad.data.model.TrackState.*
import com.squad.runsquad.util.round
import kotlinx.android.synthetic.main.activity_main.*

/**
 * The flow of initialization here is:
 *
 * User input (start button pressed) -> activity start -> startLocationUpdate() -> trackViewModelStart()
 */
class TrackActivity : AppCompatActivity() {

    private val TAG = "TrackActivity.tag"

    private val locationRequest = LocationRequest()
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationSettingsRequest: LocationSettingsRequest
    private lateinit var settingsClient: SettingsClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val trackViewModel by viewModels<TrackViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRequiredStartup()
    }

    private fun setupRequiredStartup() {
        settingsClient = LocationServices.getSettingsClient(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupLocationRequest()
        buildLocationSettingsRequest()

        setupChronometer()

        setupUIListeners()
        setupButtons()
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
                trackViewModel.updateLocation(locationResult.lastLocation)
            }
        }
    }

    private fun buildLocationSettingsRequest() {
        locationSettingsRequest = LocationSettingsRequest.Builder().apply {
            addLocationRequest(locationRequest)
        }.build()
    }

    private fun startFlow() {
        if (checkPermissions()) startLocationUpdates() else requestPermission()

        with(chronometer) {
            base = SystemClock.elapsedRealtime() - (trackViewModel.timeElapsed.value ?: 0L)
            start()
        }
    }

    private fun stopFlow() {
        stopLocationUpdates()
        trackViewModel.stop()
        //todo - reset values do whatever when it's done
        //todo - save log with view model when user is done running
    }

    private fun setupChronometer() {
        chronometer.onChronometerTickListener = Chronometer.OnChronometerTickListener {
                //do something everytime counter go up
            trackViewModel.timeElapsed.value = SystemClock.elapsedRealtime() - it.base
        }
    }

//    //todo - refactor and put this on view model cause it's business logic
//    private fun formatTime(time: Long): String {
//        val h = (time / 3600000).toInt()
//        val m = (time - h * 3600000).toInt() / 60000
//        val s = (time - h * 3600000 - m * 60000).toInt() / 1000
//        return String.format("%02d:%02d:%02d", h, m, s)
//    }

    private fun formatPace(pace: Float): String = pace.toString().replace(".", ":")

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {

        // Begin by checking if the device has the necessary location settings.
        settingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener {
                Log.d(TAG, "startLocationUpdates: All location settings are satisfied.")
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )

                trackViewModel.start()
            }
            .addOnFailureListener {

                when ((it as ApiException).statusCode) {

                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {

                        Log.d(TAG, "Location settings are not satisfied. Attempting to upgrade location settings ")

                        try {
                            (it as ResolvableApiException).startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.d(TAG, "PendingIntent unable to execute request.")
                        }
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                        val errorMessage = "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                        Log.e(TAG, errorMessage)

                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
                //todo - update ui accordingly
            }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
            .addOnCompleteListener {
                    //todo - update ui accordingly
            }
    }

    private fun setupButtons() {
        startButton.setOnClickListener {
            startFlow()
            startButton.isVisible = false
        }

        stopButton.setOnClickListener {
            stopFlow()
        }

        resumeButton.setOnClickListener {
            startFlow()
        }

        pauseButton.setOnClickListener {
            trackViewModel.pause()
        }
    }

    private fun setupUIListeners() {
        trackViewModel.isRunning.observe(this, Observer {
                when(it) {
                ACTIVE -> {
                    stopButton.isVisible = false
                    resumeButton.isVisible = false
                    pauseButton.isVisible = true
                }
                INACTIVE -> {
                    pauseButton.isVisible = false
                    stopButton.isVisible = true
                    resumeButton.isVisible = true

                    trackViewModel.timeElapsed.value = SystemClock.elapsedRealtime() - chronometer.base
                    chronometer.stop()
                    stopLocationUpdates()
                }
            }
        })

        trackViewModel.distanceTraveled.observe(this, Observer {
            distanceTravelled.text = it.round().toString()
        })

        trackViewModel.averagePace.observe(this, Observer {
            if (it.isFinite()) pace.text = formatPace(it.round())
        })

        trackViewModel.distanceTimeMediator.observe(this, Observer {
            trackViewModel.updatePace()
        })
    }

    private fun requestPermission() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object: PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    startLocationUpdates()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    // todo - implement this
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
//                    SnackbarOnDeniedPermissionListener.Builder.with(findViewById(android.R.id.content), "hahaha")
//                        .withOpenSettingsButton("Settings")
//                        .build()
//                        .onPermissionGranted(PermissionGrantedResponse())
                }
            }).check()
    }

    private fun checkPermissions(): Boolean =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CHECK_SETTINGS) {

            when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.d(TAG, "onActivityResult: User agreed to make required location settings changes.")
                }

                Activity.RESULT_CANCELED -> {
                    Log.d(TAG, "onActivityResult: User chose not to make required location settings changes.")
                    // send viewmodel to do something here
                }
            }
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

        /**
         * Constant used in the location settings dialog.
         */
        const val REQUEST_CHECK_SETTINGS = 0x1
    }
}