package com.notes.myapplication.noteactivity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.notes.myapplication.databinding.NotesItemBinding
import com.notes.myapplication.roomdb.Note

class NoteAdapter(
    private var notes: List<Note> = emptyList(),
    private val onDeleteClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var allNotes: List<Note> = notes // Full original list for filtering

    class NoteViewHolder(val binding: NotesItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NotesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        val binding = holder.binding

        binding.tvTitle.text = note.title
        binding.tvBody.text = note.body
        binding.tvDate.text = note.date

        // Delete icon click
        binding.deleteIcon.setOnClickListener {
            onDeleteClick.invoke(note)
        }

        // Setup nested RecyclerView for tags
        val tagAdapter = NotesTagAdapter(note.tags)
        binding.recyclerViewChips.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewChips.adapter = tagAdapter
    }

    override fun getItemCount() = notes.size

    fun updateNotes(newNotes: List<Note>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = notes.size
            override fun getNewListSize() = newNotes.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return notes[oldItemPosition].title == newNotes[newItemPosition].title
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return notes[oldItemPosition] == newNotes[newItemPosition]
            }
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        notes = newNotes
        diffResult.dispatchUpdatesTo(this)
    }

    // Filter notes by title, body, or tags
    fun filter(query: String) {
        val filteredList = if (query.isEmpty()) {
            allNotes
        } else {
            val lowerCaseQuery = query.lowercase()
            allNotes.filter { note ->
                note.title.lowercase().contains(lowerCaseQuery) ||
                        note.body.lowercase().contains(lowerCaseQuery) ||
                        note.tags.any { it.lowercase().contains(lowerCaseQuery) }
            }
        }
        updateNotes(filteredList)
    }

    // Call this when initially loading notes to set both lists
    fun setNotes(initialNotes: List<Note>) {
        allNotes = initialNotes
        updateNotes(initialNotes)
    }
}
