package com.notes.myapplication.noteactivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.notes.myapplication.R
import com.notes.myapplication.noteactivity.adapter.NoteAdapter
import com.notes.myapplication.noteactivity.adapter.TagFilterAdapter
import com.notes.myapplication.databinding.ActivityMainBinding
import com.notes.myapplication.databinding.BottomSheetLayoutBinding
import com.notes.myapplication.roomdb.Note
import com.notes.myapplication.utills.CommonMethods.getCurrentDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: NoteViewModel by viewModels()
    var tagList = mutableListOf<String>()
    var notes = mutableListOf<Note>()
    lateinit var filterTagAdapter: TagFilterAdapter
    lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEggeToEdge()
        initView()
    }

    fun initView() {
        initializeTagFilter()
        initializeNotesAdapter()
        listeners()
        observers()
    }

    private fun initializeTagFilter() {
        filterTagAdapter = TagFilterAdapter(tagList) { clickedTag ->
            binding.searchBoxET.setText("")
            binding.searchBoxET.clearFocus()
            val filteredNotes = if (clickedTag.isNullOrBlank()) {
                notes
            } else {
                notes.filter { it.tags.contains(clickedTag) }
            }
            noteAdapter.updateNotes(filteredNotes)
        }
        binding.filterTagsRV.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.filterTagsRV.adapter = filterTagAdapter
        noTagTextVisibility(tagList)
    }

    private fun initializeNotesAdapter() {
        noteAdapter = NoteAdapter(notes) { note ->
            viewModel.deleteNote(note)
        }
        binding.notesRV.layoutManager = LinearLayoutManager(this)
        binding.notesRV.adapter = noteAdapter
    }

    private fun listeners() {
        binding.addNoteFab.setOnClickListener {
            openAddNoteBottomSheet()
        }

        binding.searchBoxET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                noteAdapter.filter(searchText)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notes.collect { notes ->
                    this@MainActivity.notes = notes.toMutableList()
                    this@MainActivity.tagList = notes.flatMap { it.tags }.toMutableList()
                    filterTagAdapter.updateTags(tagList)
                    noTagTextVisibility(tagList)
                    noteAdapter.setNotes(notes)
                }
            }
        }
    }

    private fun openAddNoteBottomSheet() {
        val bottomSheetDialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
        val sheetBinding = BottomSheetLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(sheetBinding.root)
        // Rounded top corners
        sheetBinding.root.post {
            val parent = sheetBinding.root.parent as? View
            parent?.let {
                val shapeAppearance = ShapeAppearanceModel.builder()
                    .setTopLeftCornerSize(80f)
                    .setTopRightCornerSize(80f)
                    .build()
                val background = MaterialShapeDrawable(shapeAppearance).apply {
                    setTint(resources.getColor(R.color.screen_background, null))
                    elevation = 8f
                }
                it.background = background
            }
        }

        // Action button click
        sheetBinding.actionBtn.setOnClickListener {
            val title = sheetBinding.noteTitleET.text.toString()
            val desc = sheetBinding.noteDescET.text.toString()
            val tags = sheetBinding.noteTagET.text.toString()

            if (title.isEmpty() || desc.isEmpty() || tags.isEmpty()) {
                Toast.makeText(this, "Please Enter All Details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val tagList = tags.split(",").map { it.trim() }
            viewModel.saveNote(title, desc, getCurrentDate(), tagList)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun noTagTextVisibility(tagList: List<String>) {
        binding.noFilterTagText.visibility = if (tagList.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun enableEggeToEdge() {
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}