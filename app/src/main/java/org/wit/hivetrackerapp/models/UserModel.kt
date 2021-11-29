package org.wit.hivetrackerapp.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class UserModel(var id: Long = 0,
                     var password: String = "",
                     var firstName: String = "",
                     var secondName: String = "",
                     var image: Uri = Uri.EMPTY,
                     var userName : String = "",
                     var dateJoined: Date = Date(),
                     var email: String = "") : Parcelable


