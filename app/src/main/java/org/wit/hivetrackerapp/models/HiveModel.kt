package org.wit.hivetrackerapp.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HiveModel(var id: Long = 0,
                     var userID: Long = 0,
                     var tag: Long = 0,
                     var description: String = "",
                     var image: Uri = Uri.EMPTY,
                     var lat : Double = 0.0,
                     var lng: Double = 0.0,
                     var type: String = "",
                     var zoom: Float = 0f) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable
