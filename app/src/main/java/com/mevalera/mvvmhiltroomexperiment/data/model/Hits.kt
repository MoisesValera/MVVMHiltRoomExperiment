package com.mevalera.mvvmhiltroomexperiment.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Hits(
    val result: @RawValue Conferences
) : Parcelable

@Parcelize
data class Conferences(
    val conferencias: @RawValue List<Conference>
) : Parcelable

@Parcelize
data class HitsSingleConferences(
    val result: @RawValue SingleConferences
) : Parcelable

@Parcelize
data class SingleConferences(
    val conferences: @RawValue List<Conference>
) : Parcelable

@Parcelize
data class ConferenceFiles(
    val youtube: String? = null,
    val video: String? = null,
    val audio: String? = null,
    val pdf: String? = null,
    val pdf_simple: String? = null
) : Parcelable

@Parcelize
data class ConferenceWithBadyMap(
    val youtube: String? = null,
    val video: String? = null,
    val audio: String? = null,
    val pdf: String? = null,
    val pdf_simple: String? = null
) : Parcelable

data class ConferencesFilter(
    val year: String,
    val month: String
) {
     fun getFormattedMonth(): String {
        return if (this.month.toInt() < 10) {
            ("0${this.month}").toString()
        } else {
            this.month
        }
    }
}