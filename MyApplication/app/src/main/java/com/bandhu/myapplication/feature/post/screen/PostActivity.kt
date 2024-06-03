package com.bandhu.myapplication.feature.post.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.bandhu.myapplication.R
import com.bandhu.myapplication.database.AppDatabase
import com.bandhu.myapplication.database.RoomRepository
import com.bandhu.myapplication.databinding.ActivityMainBinding
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
            application, appDatabase,
            RemoteRepository.getInstance(application),
            RoomRepository.getInstance(
                application
            )
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
            viewModel.data.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }

        }
    }

    private fun startWorker() {
        // Create a OneTimeWorkRequest
        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(ScheduleWorker::class.java, 15, TimeUnit.MINUTES).build()
        // Enqueue the work
        WorkManager.getInstance(this).enqueue(periodicWorkRequest)


    }
}