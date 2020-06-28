package com.squad.runsquad.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.MutableLiveData
import java.math.RoundingMode

fun MutableLiveData<Float>.aggregateValue(value: Float) {
    if (this.value == null) this.postValue(value.round())
    else this.postValue((this.value!! + value).round())
}

fun Float.round(): Float =
    this.toBigDecimal().setScale(2, RoundingMode.UP).toFloat()

/**
 * Allows calls like
 *
 * `viewGroup.inflate(R.layout.foo)`
 */
fun ViewGroup.inflate(@LayoutRes layout: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layout, this, attachToRoot)
}