package com.squad.runsquad.repository

import com.squad.runsquad.data.model.User
import com.squad.runsquad.data.remotesource.UserRemoteSource

class UserRepository(private val userRemoteSource: UserRemoteSource) {

    fun createUser(user: User, onSuccess: ()-> Unit) {
            userRemoteSource.createUser(user, onSuccess)
    }

    fun getCurrentUser(onSuccess: (User) -> Unit) {
        userRemoteSource.getCurrentUser(onSuccess)
    }
}