package com.squad.runsquad.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.util.ExtraConstants
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.squad.runsquad.ui.track.TrackActivity
import org.koin.android.ext.android.inject


class LaunchOrchestratorActivity: AppCompatActivity() {

    private val TAG = "LaunchOrchestratorActiv"

    private val auth: FirebaseAuth by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userRoute()
        finish()
    }

    private fun userRoute() {
        auth.currentUser?.let {
            startActivity(Intent(this, TrackActivity::class.java))
        } ?: kotlin.run {

            val actionCodeSettings = ActionCodeSettings.newBuilder()
                .setAndroidPackageName("com.squad.runsquad", true, null)
                .setHandleCodeInApp(true) // This must be set to true
                .setUrl("runsquad-b5a8a.firebaseapp.com") // This URL needs to be whitelisted
                .build();

            val emailBuilder = AuthUI.IdpConfig.EmailBuilder()
                .enableEmailLinkSignIn()
                .setActionCodeSettings(actionCodeSettings)

            val providers = arrayListOf(
                emailBuilder.build(),
                AuthUI.IdpConfig.PhoneBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
            )

            // Create and launch sign-in intent
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Log.d(TAG, "onActivityResult: sign in failed - Error: ${response?.error}")
            }
        }
    }

    private companion object {
        const val RC_SIGN_IN = 123
    }
}