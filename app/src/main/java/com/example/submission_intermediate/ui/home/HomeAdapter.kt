package com.example.submission_intermediate.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission_intermediate.databinding.ItemStoryBinding
import com.example.submission_intermediate.service.response.ListStoryItem
import com.example.submission_intermediate.ui.detail.DetailActivity
import com.example.submission_intermediate.uitls.CustomViewHelper


class HomeAdapter(private val context: Context) : ListAdapter<ListStoryItem, HomeAdapter.MyViewHolder>(StoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    inner class MyViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem){

            binding.storyName.text = story.name
            binding.uploadDate.text = CustomViewHelper.formatCreatedAt(story.createdAt)
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

    class StoryDiffCallback : DiffUtil.ItemCallback<ListStoryItem>() {
        override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        const val UID = "extra_id"
    }
}

