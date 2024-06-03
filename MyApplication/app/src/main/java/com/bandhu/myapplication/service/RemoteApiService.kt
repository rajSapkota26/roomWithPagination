package com.bandhu.myapplication.service

import com.bandhu.myapplication.feature.model.PostResponse
import com.bandhu.myapplication.retrofit.DefaultResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RemoteApiService {

    @GET("posts")
    suspend fun sendFindPostsRequest(@Query("limit")limit:Int,@Query("skip")skip:Int): Response<PostResponse>
    suspend fun sendFindAllPostsRequest(): Response<PostResponse>



}