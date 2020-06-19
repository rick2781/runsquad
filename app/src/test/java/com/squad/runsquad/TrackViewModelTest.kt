package com.squad.runsquad

import android.location.Location
import android.util.Log
import com.google.common.truth.Truth.assertThat
import com.squad.runsquad.ui.track.TrackViewModel
import com.squad.runsquad.util.round
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.lang.AssertionError

@RunWith(RobolectricTestRunner::class)
class TrackViewModelTest {

    //todo - Update tests with hamcrest or something else to be better and more consistent

    private lateinit var trackViewModel: TrackViewModel

    private lateinit var location: Location

    @Before
    fun setupTest() {
        trackViewModel = TrackViewModel()

        location = Location("firstLocation").apply {
            latitude = 39.604466
            longitude = -104.684404
        }
    }

    @Test
    fun testDistanceAggregationWhenInitialized() {

        trackViewModel.updateLocation(location)

        assert(trackViewModel.distanceTraveled.getOrAwaitValue() == 0F)
    }

    @Test
    fun testDistanceAggregationWhenActivityInProgress() {

        trackViewModel.updateLocation(location)

        val lastLocation = Location("lastLocation").apply {
            latitude = 39.611951
            longitude = -104.709856
        }

        trackViewModel.updateLocation(lastLocation)
        assert(trackViewModel.distanceTraveled.getOrAwaitValue() == 2.34F)
    }

    @Test
    fun testDistanceAggregationWhenActivityInProgressTwice() {

        trackViewModel.updateLocation(location)

        val middleLocation = Location("middleLocation").apply {
            latitude = 39.611951
            longitude = -104.709856
        }

        trackViewModel.updateLocation(middleLocation)

        val lastLocation = Location("lastLocation").apply {
            latitude = 39.604466
            longitude = -104.684404
        }

        trackViewModel.updateLocation(lastLocation)
        //you need to figure out why after the second update the live data value is 0.0. Maybe because no one is listening to it??

//        throw AssertionError(middleLocation.distanceTo(lastLocation))
        assert(trackViewModel.distanceTraveled.getOrAwaitValue() == 4.68F)
    }

//    @Test
//    fun testAveragePaceCalculation() {
//
//        trackViewModel.calculateAveragePace(600000, 5.00F)
//
//        throw AssertionError(trackViewModel.averagePace.getOrAwaitValue())
////        assert( == 20F)
//    }
}