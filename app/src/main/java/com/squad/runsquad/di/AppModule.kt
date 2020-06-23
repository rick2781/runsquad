package com.squad.runsquad.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squad.runsquad.repository.TrackRepository
import org.koin.dsl.module

val appModule = module {

    /**
     * Repository Section
     */

    single { TrackRepository() }


    /**
     * Util Objects Section
     */

    single { Firebase.firestore }
    single { FirebaseAuth.getInstance() }

}