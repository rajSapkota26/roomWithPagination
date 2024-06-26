package com.bandhu.myapplication.service

import com.bandhu.myapplication.feature.post.model.PostResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteApiService {

    @GET("posts")
    suspend fun sendFindPostsRequest(@Query("limit")limit:Int,@Query("skip")skip:Int): Response<PostResponse>
    @GET("posts")
    suspend fun sendFindAllPostsRequest(): Response<PostResponse>



}