package com.mevalera.mvvmhiltroomexperiment.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Hits(
    val hits: @RawValue List<Restaurant>
) : Parcelable