package com.squad.runsquad

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ForegroundService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}