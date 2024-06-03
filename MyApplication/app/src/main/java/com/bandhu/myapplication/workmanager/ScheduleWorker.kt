package com.bandhu.myapplication.workmanager

import android.app.Application
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bandhu.myapplication.database.RoomRepository
import com.bandhu.myapplication.feature.post.model.PostResponse
import com.bandhu.myapplication.repository.RemoteRepository
import com.example.ride.retrofit.RemoteApiGeneralResponse

class ScheduleWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        fetchDataAndSaveDB(context)
        return Result.success()
    }

    private suspend fun fetchDataAndSaveDB(context: Context) {
        val repository = RemoteRepository.getInstance(context as Application)
        repository.sendFindAllPostsRequest().collect {
            if (it.status == RemoteApiGeneralResponse.Status.SUCCESS) {
                //save to local db
                it.data?.let { data ->
                    //saved

                }
            }
        }


    }



}