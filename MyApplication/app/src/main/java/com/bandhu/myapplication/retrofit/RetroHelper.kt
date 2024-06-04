package com.bandhu.myapplication.retrofit

import android.content.Context
import kotlinx.coroutines.flow.flow
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber

class RetroHelper<T> {
    companion object {
        const val SERVER_ERROR_MESSAGE = "Something went wrong, please try again!!"
    }

    suspend fun enqueue(
        context: Context,
        response: Response<T>,
        saveOnDB: Boolean = false,
        filterNotifyUserJson: Boolean = false
    ) = flow<RemoteApiGeneralResponse<T>> {
        try {
            Timber.v("status code: %s", response.code())
            Timber.v("status code: %s", response.body())
            if (response.isSuccessful || response.code() == 201) {
                if (saveOnDB) {
                    emit(RemoteApiGeneralResponse.saveOnDB(response.body()!!))
                } else {
                    val responseBody = response.body()
                    if (filterNotifyUserJson && responseBody != null) {
                        emit(RemoteApiGeneralResponse.success(NotifyUtils.notify(responseBody.toString()) as T))
                    } else {
                        emit(RemoteApiGeneralResponse.success(responseBody ?: "Success" as T))
                    }
                }
            } else {
                handleErrorResponse(response) { emit(it) }
            }
        } catch (ex: Exception) {
            Timber.e(ex, "Retro-helper exception: %s", ex.message)
            emit(RemoteApiGeneralResponse.onException("Internal Server error, Please try again"))
        }
    }

    private inline fun handleErrorResponse(
        response: Response<T>,
        emitFunction: (RemoteApiGeneralResponse<T>) -> Unit
    ) {
        val responseBody = response.errorBody()
        if (responseBody != null) {
            when (response.code()) {
                in 400..499 -> {
                    try {
                        val jsonObject = JSONObject(responseBody.string())
                        val message = jsonObject.optString("message", SERVER_ERROR_MESSAGE)
                        if (response.code() == 401) {
                            emitFunction(RemoteApiGeneralResponse.tokenExpired(message))
                        } else {
                            emitFunction(RemoteApiGeneralResponse.error(message))
                        }
                    } catch (ex: JSONException) {
                        Timber.e(ex, "Error parsing JSON response: %s", ex.message)
                        emitFunction(RemoteApiGeneralResponse.onException("Error parsing error response"))
                    }
                }

                in 500..599 -> {
                    emitFunction(RemoteApiGeneralResponse.error("Internal Server error, Please try again"))
                    Timber.v("internal server error %s%s", "${response.code()}: ", responseBody.string())
                }

                else -> emitFunction(RemoteApiGeneralResponse.error(SERVER_ERROR_MESSAGE))
            }
        } else {
            emitFunction(RemoteApiGeneralResponse.error(SERVER_ERROR_MESSAGE))
        }
    }

    // Combine these two methods into one, accepting a Response<T>
    private fun removeDoubleQuotes(response: Response<T>): String? {
        Timber.v("%s", response.body())
        val result = response.body()?.toString()
        return result?.removeSurrounding("\"")
    }
}
