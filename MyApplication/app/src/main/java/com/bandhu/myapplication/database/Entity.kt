package com.bandhu.myapplication.database

import androidx.room.*

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val body: String,
    val views: Long,
    val userID: Long,
    @Embedded val reactions: ReactionsEntity
)

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val tagId: Long = 0,
    val postId: Long,
    val tag: String
)

data class ReactionsEntity(
    val likes: Long,
    val dislikes: Long
)

data class PostWithTags(
    @Embedded val post: PostEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "postId"
    )
    val tags: List<TagEntity>
)
