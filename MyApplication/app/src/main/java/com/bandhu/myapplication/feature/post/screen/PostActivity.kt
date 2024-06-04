package com.bandhu.myapplication.feature.post.screen

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.bandhu.myapplication.database.AppDatabase
import com.bandhu.myapplication.databinding.ActivityPostBinding
import com.bandhu.myapplication.feature.post.adapter.MainAdapter
import com.bandhu.myapplication.feature.post.adapter.PostPageAdapter
import com.bandhu.myapplication.feature.post.vm.PostViewModelFactory
import com.bandhu.myapplication.feature.post.vm.PostVm
import com.bandhu.myapplication.repository.RemoteRepository
import com.bandhu.myapplication.workmanager.ScheduleWorker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private lateinit var appDatabase: AppDatabase
    private lateinit var adapter: PostPageAdapter
    private val viewModel: PostVm by viewModels {
        PostViewModelFactory(
            appDatabase,
            RemoteRepository.getInstance(application)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDatabase = AppDatabase.getDatabase(this)

        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.loadPosts()
        adapter = PostPageAdapter()
        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            MainAdapter()
        )


        loadData()
        startWorker()
    }

    private fun loadData() {
        lifecycleScope.launch {
            adapter.addLoadStateListener { loadState ->
                loadState.decideOnState(
                    showLoading = {
                        binding.progress.visibility=View.VISIBLE

                    },
                    showEmptyState = { visible ->
                        Toast.makeText(this@PostActivity, visible.toString(), Toast.LENGTH_SHORT).show()
                        binding.progress.visibility=View.GONE
                                     },
                    showError = { message ->
                        Toast.makeText(this@PostActivity, message, Toast.LENGTH_SHORT).show()
                        binding.progress.visibility=View.GONE
                    }
                )
            }
        }
        //need to declare if user use app first time we user first call from server after second use room db
        //at this time we have already data in room db so worker handle
        viewModel.list.observe(this@PostActivity) { response ->
            response?.let {
                lifecycleScope.launch {
                    viewModel.data.collectLatest { pagingData ->
                        binding.progress.visibility=View.GONE
                        adapter.submitData(pagingData)
                    }
                }


            }


        }
    }

    private fun startWorker() {
        // Create a periodicWorkRequest
        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(ScheduleWorker::class.java, 15, TimeUnit.MINUTES).build()
        // Enqueue the work
        WorkManager.getInstance(this).enqueue(periodicWorkRequest)


    }
    private inline fun CombinedLoadStates.decideOnState(
        showLoading: (Boolean) -> Unit,
        showEmptyState: (Boolean) -> Unit,
        showError: (String) -> Unit
    ) {
        showLoading(refresh is LoadState.Loading)

        showEmptyState(
            source.append is LoadState.NotLoading
                    && source.append.endOfPaginationReached
                    && adapter.itemCount == 0
        )

        val errorState = source.append as? LoadState.Error
            ?: source.prepend as? LoadState.Error
            ?: source.refresh as? LoadState.Error
            ?: append as? LoadState.Error
            ?: prepend as? LoadState.Error
            ?: refresh as? LoadState.Error

        errorState?.let { showError(it.error.toString()) }
    }
}