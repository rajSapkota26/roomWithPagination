package com.bandhu.myapplication.retrofit


import org.json.JSONObject
import timber.log.Timber


object NotifyUtils {

    fun notify(json: String): String {
        val jsonObject = JSONObject(json)
        try {
            return jsonObject.getString("notifyUser")
        } catch (ex: Exception) {
            Timber.v("Couldn't parse notifyUser")
        }

        return ""
    }
}