package com.bandhu.myapplication.retrofit

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DefaultResponse<T> : Serializable {

    @SerializedName("message")
    var message: String? = null

    @SerializedName("data")
    var data: T? = null
}