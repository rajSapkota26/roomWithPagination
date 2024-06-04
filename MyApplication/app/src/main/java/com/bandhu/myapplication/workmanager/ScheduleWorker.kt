package com.bandhu.myapplication.workmanager

import android.app.Application
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bandhu.myapplication.repository.RemoteRepository
import com.bandhu.myapplication.retrofit.RemoteApiGeneralResponse
import timber.log.Timber

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
                    Timber.v("data ${data.posts}")

                }
            }
        }


    }



}