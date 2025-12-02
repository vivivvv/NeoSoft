package com.notes.myapplication.noteactivity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.notes.myapplication.databinding.NoteTagItemBinding

class NotesTagAdapter(
    private val items: List<String>
) : RecyclerView.Adapter<NotesTagAdapter.TagViewHolder>() {

    class TagViewHolder(val binding: NoteTagItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val binding =
            NoteTagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = items[position]
        holder.binding.tvTag.text = tag
    }

    override fun getItemCount() = items.size
}
