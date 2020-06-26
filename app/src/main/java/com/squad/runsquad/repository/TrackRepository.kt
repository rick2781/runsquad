package com.squad.runsquad.repository

import com.google.firebase.auth.FirebaseAuth
import com.squad.runsquad.data.model.Track
import com.squad.runsquad.data.remotesource.TrackRemoteSource
import com.squad.runsquad.data.remotesource.UserRemoteSource

class TrackRepository(private val trackRemoteSource: TrackRemoteSource, private val auth: FirebaseAuth) {

    fun getAllTracks(onSuccess: (ArrayList<Track>) -> Unit) =
        auth.currentUser?.let {
            trackRemoteSource.getAllTracks(it.uid) { trackList ->
                onSuccess.invoke(trackList)
            }
        }

    fun saveTrack(track: Track) =
        auth.currentUser?.let {
            trackRemoteSource.saveTrack(track, it.uid)
        }
}