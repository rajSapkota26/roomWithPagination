package com.bandhu.myapplication.feature.post.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.bandhu.myapplication.database.AppDatabase
import com.bandhu.myapplication.database.RoomRepository
import com.bandhu.myapplication.feature.post.model.PostPagingSource
import com.bandhu.myapplication.feature.post.model.PostResponse
import com.bandhu.myapplication.repository.RemoteRepository
import com.example.ride.retrofit.RemoteApiGeneralResponse
import kotlinx.coroutines.launch

class PostVm(
    private val application: Application,
    private val appDatabase: AppDatabase,
    private val remoteRepository: RemoteRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {

    var data = Pager(
        PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            initialLoadSize = 10
        )
    ) {
        PostPagingSource(appDatabase)
    }.flow.cachedIn(viewModelScope)

   var list=MutableLiveData<RemoteApiGeneralResponse<PostResponse>?>(null)



    fun loadPosts() {
        viewModelScope.launch {
            remoteRepository.sendFindPostsRequest().collect {
                list.postValue(it)

            }
        }

    }


}

class PostViewModelFactory(
    private val application: Application, private val appDatabase: AppDatabase,
    private val remoteRepository: RemoteRepository,
    private val roomRepository: RoomRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostVm::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostVm(application, appDatabase, remoteRepository, roomRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}