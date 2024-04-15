package com.example.submission_intermediate.ui.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission_intermediate.databinding.ItemStoryBinding
import com.example.submission_intermediate.service.response.StoryDB
import com.example.submission_intermediate.ui.detail.DetailActivity
import com.example.submission_intermediate.uitls.Helper


class HomeAdapter(private val context: Context) : PagingDataAdapter<StoryDB, HomeAdapter.MyViewHolder>(StoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }

    }
    inner class MyViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryDB){
            binding.storyName.text = story.name
            binding.uploadDate.text = Helper.formatCreatedAt(story.createdAt)
            binding.imgStory.loadImage(url = story.photoUrl)

            binding.itemStory.setOnClickListener{
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(UID, story.id)

                context.startActivity(intent)
            }
        }

        private fun ImageView.loadImage(url: String) {
            Glide.with(this.context)
                .load(url)
                .centerCrop()
                .into(this)
        }
    }

    class StoryDiffCallback : DiffUtil.ItemCallback<StoryDB>() {
        override fun areItemsTheSame(oldItem: StoryDB, newItem: StoryDB): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: StoryDB, newItem: StoryDB): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        const val UID = "extra_id"
    }
}

