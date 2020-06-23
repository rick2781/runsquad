package com.squad.runsquad

import android.app.Application
import com.squad.runsquad.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RunSquadApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@RunSquadApp)
            modules(appModule)
        }
    }
}