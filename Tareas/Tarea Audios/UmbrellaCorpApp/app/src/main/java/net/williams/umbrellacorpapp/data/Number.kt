package net.williams.umbrellacorpapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Number (
    val nameNumber: String,
    val pronunciationNumber: String,
    val imageNumberId: Int,
    val audioNumberId: Int
) : Parcelable