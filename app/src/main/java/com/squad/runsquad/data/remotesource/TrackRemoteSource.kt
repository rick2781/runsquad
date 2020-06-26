package com.squad.runsquad.data.remotesource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import com.squad.runsquad.data.model.Track
import com.squad.runsquad.util.USER_COLLECTION
class TrackRemoteSource(private val database: FirebaseFirestore) {

    fun saveTrack(track: Track, userId: String) {
        val doc = getTrackCollectionQuery(userId).document()
        doc.set(track.apply { trackId = doc.id })
    }

    fun getAllTracks(userId: String, onSuccess: (ArrayList<Track>) -> Unit) {
        getTrackCollectionQuery(userId)
            .orderBy(TIMESTAMP, Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { onSuccess.invoke(ArrayList(it.toObjects())) }
    }

    private fun getTrackCollectionQuery(userId: String) = database.collection(USER_COLLECTION).document(userId).collection(TRACK_COLLECTION)

    private companion object {
        const val TRACK_COLLECTION = "tracks"
        const val TIMESTAMP = "timestamp"
    }
}

