package com.squad.runsquad

import android.location.Location
import com.squad.runsquad.ui.track.TrackViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TrackViewModelTest {

    private lateinit var trackViewModel: TrackViewModel

    private lateinit var location: Location

    @Before
    fun setupTest() {
        trackViewModel = TrackViewModel()
    }

    @Test
    fun testDistanceAggregationWhenInitialized() {

        location = Location("firstLocation").apply {
            latitude = 39.604466
            longitude = -104.684404
        }

        trackViewModel.updateLocation(location)

        assert(trackViewModel.distanceTraveled.getOrAwaitValue() == 0F)
    }
}