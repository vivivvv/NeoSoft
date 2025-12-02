package com.notes.myapplication.noteactivity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.notes.myapplication.databinding.FilterTagItemBinding

class TagFilterAdapter(
    private var items: List<String>,
    private val onItemClick: ((String) -> Unit)
) : RecyclerView.Adapter<TagFilterAdapter.CardViewHolder>() {

    class CardViewHolder(val binding: FilterTagItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = FilterTagItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvTag.text = item

        holder.binding.root.setOnClickListener {
            onItemClick.invoke(item)
        }
    }

    override fun getItemCount() = items.size

    fun updateTags(newTags: List<String>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = items.size
            override fun getNewListSize() = newTags.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition] == newTags[newItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition] == newTags[newItemPosition]
            }
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newTags
        diffResult.dispatchUpdatesTo(this)
    }
}

