package com.bandhu.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bandhu.myapplication.database.AppDatabase
import com.bandhu.myapplication.database.RoomRepository
import com.bandhu.myapplication.databinding.ActivityMainBinding
import com.bandhu.myapplication.feature.post.adapter.PostPageAdapter
import com.bandhu.myapplication.feature.post.screen.PostActivity
import com.bandhu.myapplication.feature.post.vm.PostViewModelFactory
import com.bandhu.myapplication.feature.post.vm.PostVm
import com.bandhu.myapplication.repository.RemoteRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
            startActivity(Intent(this@MainActivity, PostActivity::class.java))
        }


    }


}