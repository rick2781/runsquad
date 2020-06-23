package com.squad.runsquad.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squad.runsquad.repository.TrackRepository
import com.squad.runsquad.ui.track.TrackViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    /**
     * Repository Section
     */

    single { TrackRepository() }

    /**
     * View Model Section
     */

    viewModel { TrackViewModel(get()) }

    /**
     * Util Objects Section
     */

    single { Firebase.firestore }
    single { FirebaseAuth.getInstance() }

}