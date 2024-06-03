package com.bandhu.myapplication.repository

import android.app.Application
import android.content.Context
import com.bandhu.myapplication.feature.model.PostResponse
import com.bandhu.myapplication.retrofit.DefaultResponse
import com.bandhu.myapplication.service.RemoteApiService
import com.bandhu.myapplication.utill.NetworkUtils
import com.example.ride.retrofit.NetworkModule
import com.example.ride.retrofit.RemoteApiGeneralResponse
import com.example.ride.retrofit.RetroHelper
import kotlinx.coroutines.flow.flow
import retrofit2.http.Query
import timber.log.Timber
import java.net.UnknownHostException

class RemoteRepository private constructor(
    private val applicationContext: Context,
    private val remoteApiService: RemoteApiService,
) {

    companion object {
        private var instance: RemoteRepository? = null

        fun getInstance(application: Application): RemoteRepository {
            return instance ?: synchronized(this) {
                instance ?: RemoteRepository(
                    application.applicationContext,
                    NetworkModule.provideApiService(application)
                ).also { instance = it }
            }
        }
    }


    suspend fun sendFindAllPostsRequest()= flow<RemoteApiGeneralResponse<PostResponse> >{
        try {
            Timber.v("inside common repo")
            if (!NetworkUtils.hasInternetConnection(applicationContext)) {

                return@flow
            }
            val helper = RetroHelper<PostResponse>()
            helper.enqueue(
                applicationContext,
                remoteApiService.sendFindAllPostsRequest()
            ).collect {
                Timber.v("data ${it.toString()}")
                emit(it)

            }

        } catch (ex: UnknownHostException) {
            Timber.v("$ex")
            emit(RemoteApiGeneralResponse.error("UnKnown host exception"))
        } catch (ex: Exception) {
            Timber.v("on exception%s", ex.message)
            emit(RemoteApiGeneralResponse.onException(ex.message))
        }
        return@flow
    }

}