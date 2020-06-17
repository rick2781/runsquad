package com.squad.runsquad.util

import androidx.lifecycle.MutableLiveData

fun MutableLiveData<Float>.aggregateValue(value: Float) {
    if (this.value != null) this.postValue(value)
    else this.postValue(this.value!! + value)
}