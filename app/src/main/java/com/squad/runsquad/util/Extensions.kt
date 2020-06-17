package com.squad.runsquad.util

import androidx.lifecycle.MutableLiveData
import java.math.RoundingMode

fun MutableLiveData<Float>.aggregateValue(value: Float) {
    if (this.value == null) this.postValue(value)
    else this.postValue(this.value!! + value)
}

fun Float.round(): Float =
    this.toBigDecimal().setScale(2, RoundingMode.UP).toFloat()