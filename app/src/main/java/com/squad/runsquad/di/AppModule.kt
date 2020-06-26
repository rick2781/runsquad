package com.squad.runsquad.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squad.runsquad.data.remotesource.TrackRemoteSource
import com.squad.runsquad.data.remotesource.UserRemoteSource
import com.squad.runsquad.repository.TrackRepository
import com.squad.runsquad.repository.UserRepository
import com.squad.runsquad.ui.track.TrackViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    /**
     * Remote Source Section
     */

    single { UserRemoteSource(get(), get()) }
    single { TrackRemoteSource(get()) }

    /**
     * Repository Section
     */

    single { TrackRepository(get(), get()) }
    single { UserRepository(get()) }

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