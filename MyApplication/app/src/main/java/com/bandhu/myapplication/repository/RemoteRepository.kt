package com.bandhu.myapplication.repository

import android.app.Application
import android.content.Context
import com.bandhu.myapplication.database.RoomRepository
import com.bandhu.myapplication.feature.post.model.PostResponse
import com.bandhu.myapplication.service.RemoteApiService
import com.bandhu.myapplication.utill.NetworkUtils
import com.example.ride.retrofit.NetworkModule
import com.bandhu.myapplication.retrofit.RemoteApiGeneralResponse
import com.bandhu.myapplication.retrofit.RetroHelper
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.net.UnknownHostException

class RemoteRepository private constructor(
    private val applicationContext: Context,
    private val remoteApiService: RemoteApiService,
    private val roomRepository: RoomRepository
) {

    companion object {
        private var instance: RemoteRepository? = null

        fun getInstance(application: Application): RemoteRepository {
            return instance ?: synchronized(this) {
                instance ?: RemoteRepository(
                    application.applicationContext,
                    NetworkModule.provideApiService(application),
                    RoomRepository.getInstance(application)
                ).also { instance = it }
            }
        }
    }


    suspend fun sendFindAllPostsRequest() = flow<RemoteApiGeneralResponse<PostResponse>> {
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
                it.data?.let {data->
                    roomRepository.savePost(data)
                }
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

    suspend fun sendFindPostsRequest() = flow<RemoteApiGeneralResponse<PostResponse>> {
        try {
            Timber.v("inside common repo")
            if (!NetworkUtils.hasInternetConnection(applicationContext)) {

                return@flow
            }
            val helper = RetroHelper<PostResponse>()
            helper.enqueue(
                applicationContext,
                remoteApiService.sendFindPostsRequest(10, 0)
            ).collect {
                it.data?.let {data->
                    roomRepository.savePost(data)
                }
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