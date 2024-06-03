package com.bandhu.myapplication.feature.post.screen

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.bandhu.myapplication.database.AppDatabase
import com.bandhu.myapplication.database.PostEntity
import com.bandhu.myapplication.database.RoomRepository
import com.bandhu.myapplication.databinding.ActivityMainBinding
import com.bandhu.myapplication.feature.post.adapter.MainAdapter
import com.bandhu.myapplication.feature.post.adapter.PostPageAdapter
import com.bandhu.myapplication.feature.post.vm.PostViewModelFactory
import com.bandhu.myapplication.feature.post.vm.PostVm
import com.bandhu.myapplication.repository.RemoteRepository
import com.bandhu.myapplication.utill.SharedPref
import com.bandhu.myapplication.workmanager.ScheduleWorker
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
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

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loadPosts()

        lifecycleScope.launch {
            delay(3000)
            startActivity(Intent(this@MainActivity,PostActivity::class.java))
        }


    }


}