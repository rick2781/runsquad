package com.squad.runsquad.data.remotesource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.squad.runsquad.data.model.User
import com.squad.runsquad.util.USER_COLLECTION

class UserRemoteSource(private val database: FirebaseFirestore, private val auth: FirebaseAuth) {

    fun getCurrentUser(onSuccess: (User) -> Unit) =
        auth.currentUser?.uid?.let {
            database.collection(USER_COLLECTION)
                .document(it)
                .get()
                .addOnSuccessListener { snapshot ->
                    onSuccess.invoke(snapshot.toObject()!!)
                }
        }

    fun createUser(user: User, onSuccess: () -> Unit) {
        database.collection(USER_COLLECTION)
            .document(user.userId)
            .set(user)
            .addOnSuccessListener { onSuccess.invoke() }
    }
}