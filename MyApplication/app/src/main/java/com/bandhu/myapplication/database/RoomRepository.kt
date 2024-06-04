package com.bandhu.myapplication.database

import android.app.Application
import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.bandhu.myapplication.feature.post.model.PostPagingSource
import com.bandhu.myapplication.feature.post.model.PostResponse
import com.bandhu.myapplication.repository.RemoteRepository
import com.example.ride.retrofit.NetworkModule

class RoomRepository private constructor(
    private val database: AppDatabase,
) {
    companion object {
        private var instance: RoomRepository? = null

        fun getInstance(application: Application): RoomRepository {
            return instance ?: synchronized(this) {
                instance ?: RoomRepository(
                    AppDatabase.getDatabase(application.applicationContext)
                ).also { instance = it }
            }
        }
    }
     suspend fun savePost(postResponse: PostResponse) {
        val postDao = database.postDao()
        val tagDao = database.tagDao()

        postResponse.posts.forEach { post ->
            val postEntity = PostEntity(
                id = post.id,
                title = post.title,
                body = post.body,
                views = post.views,
                userID = post.userID,
                reactions = ReactionsEntity(
                    likes = post.reactions.likes,
                    dislikes = post.reactions.dislikes
                )
            )

            postDao.insertPost(postEntity)
            tagDao.deleteTagsForPost(post.id)
            val tags = post.tags.map { tag ->
                TagEntity(postId = post.id, tag = tag)
            }
            postDao.insertTags(tags)
        }


    }

      fun getPagingPosts(): PostPagingSource {


       return PostPagingSource(database);

     }









}