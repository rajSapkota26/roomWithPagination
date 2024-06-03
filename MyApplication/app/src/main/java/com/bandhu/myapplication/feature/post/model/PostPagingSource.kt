package com.bandhu.myapplication.feature.post.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bandhu.myapplication.database.AppDatabase
import com.bandhu.myapplication.database.PostEntity
import com.bandhu.myapplication.database.PostWithTags

class PostPagingSource(private val appDatabase: AppDatabase) : PagingSource<Int, PostEntity>() {
    override fun getRefreshKey(
        state: PagingState<Int, PostEntity>
    ): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostEntity> {
        val page = params.key ?: 0

        return try {

            val pagedList =
                appDatabase.postDao().getPagedList(params.loadSize, page * params.loadSize)

            if (pagedList.isEmpty()){

            }




            LoadResult.Page(
                data = pagedList,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (pagedList.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}