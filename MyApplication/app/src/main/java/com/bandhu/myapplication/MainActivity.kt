package com.bandhu.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.bandhu.myapplication.database.AppDatabase
import com.bandhu.myapplication.databinding.ActivityMainBinding
import com.bandhu.myapplication.feature.adapter.MainAdapter
import com.bandhu.myapplication.feature.adapter.PostPageAdapter
import com.bandhu.myapplication.feature.vm.PostViewModelFactory
import com.bandhu.myapplication.feature.vm.PostVm
import com.bandhu.myapplication.workmanager.ScheduleWorker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appDatabase: AppDatabase
    private val viewModel: PostVm by viewModels { PostViewModelFactory(application, appDatabase) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDatabase = AppDatabase.getDatabase(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostPageAdapter()
        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            MainAdapter()
        )

        lifecycleScope.launch {
            viewModel.data.collectLatest {
                adapter.submitData(it)
            }
        }
        startWorker()
    }

    private fun startWorker() {
       // Create a OneTimeWorkRequest
       val periodicWorkRequest = PeriodicWorkRequest.Builder(ScheduleWorker::class.java, 15, TimeUnit.MINUTES).build()
       // Enqueue the work
        WorkManager.getInstance(this).enqueue(periodicWorkRequest)
    }
}