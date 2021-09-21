package com.sphe.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategorySongData(
    val title: String,
    val songCount: Int,
    val type: Int,
    val id: Long
) : Parcelable
