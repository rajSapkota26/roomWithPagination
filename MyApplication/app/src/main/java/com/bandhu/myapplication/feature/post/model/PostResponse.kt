package com.bandhu.myapplication.feature.post.model

import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName("posts") val posts: List<Post>,
    @SerializedName("total") val total: Long,
    @SerializedName("skip") val skip: Long,
    @SerializedName("limit") val limit: Long
) {
    data class Post(
        @SerializedName("id") val id: Long,
        @SerializedName("title") val title: String,
        @SerializedName("body") val body: String,
        @SerializedName("tags") val tags: List<String>,
        @SerializedName("reactions") val reactions: Reactions,
        @SerializedName("views") val views: Long,
        @SerializedName("userID") val userID: Long
    ) {
        data class Reactions(
            @SerializedName("likes") val likes: Long,
            @SerializedName("dislikes") val dislikes: Long
        )
    }
}