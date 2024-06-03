package com.bandhu.myapplication.feature.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.bandhu.myapplication.database.AppDatabase
import com.bandhu.myapplication.feature.model.PostPagingSource

class PostVm(application: Application, appDatabase: AppDatabase) : ViewModel() {
    val data = Pager(
        PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
            initialLoadSize = 20
        ),
    ) {
        PostPagingSource(appDatabase)
    }.flow.cachedIn(viewModelScope)


}

class PostViewModelFactory(
    private val application: Application, private val appDatabase: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostVm::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostVm(application, appDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}