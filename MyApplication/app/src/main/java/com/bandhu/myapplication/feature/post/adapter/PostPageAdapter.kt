package com.bandhu.myapplication.feature.post.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bandhu.myapplication.database.PostEntity
import com.bandhu.myapplication.database.PostWithTags
import com.bandhu.myapplication.databinding.PostItemBinding
import com.bandhu.myapplication.feature.post.model.PostResponse

class PostPageAdapter:PagingDataAdapter<PostEntity, PostPageAdapter.PostViewHolder>(DIFF_CALLBACK) {


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PostEntity>() {
            override fun areItemsTheSame(oldItem: PostEntity, newItem: PostEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PostEntity, newItem: PostEntity): Boolean =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.apply {
            textView.text = item?.body
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {





        return PostViewHolder(
            PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    class PostViewHolder(val binding: PostItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

}