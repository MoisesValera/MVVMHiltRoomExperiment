package com.mevalera.mvvmhiltroomexperiment.util

import android.content.res.Resources
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import timber.log.Timber
import kotlin.math.roundToInt

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

/*
 DP to Pixels
 */
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()