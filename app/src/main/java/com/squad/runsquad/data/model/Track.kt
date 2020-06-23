package com.squad.runsquad.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Track(
    var trackId: String = "",
    var distanceTravelled: Float = 0F,
    var timeElapsed: Long = 0L,
    var averagePace: Float = 0F,
    @ServerTimestamp
    var timestamp: Date? = null
)

/**
 * features to be added in the future
 */
//var initialLocation: Location = Location("placeHolderLocationObject"),
//var finalLocation: Location = Location("placeHolderLocationObject"),
//var elevation,
//var performanceGraph