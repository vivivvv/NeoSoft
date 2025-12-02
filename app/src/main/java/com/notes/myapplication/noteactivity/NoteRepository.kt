package com.notes.myapplication.noteactivity

import com.notes.myapplication.roomdb.Note
import com.notes.myapplication.roomdb.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDatabase: NoteDatabase) {

    fun getAllNotes(): Flow<List<Note>> = noteDatabase.noteDao().getAllNotes()

    suspend fun insertNote(note: Note) {
        withContext(Dispatchers.IO) {
            noteDatabase.noteDao().insert(note)
        }
    }

    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            noteDatabase.noteDao().delete(note)
        }
    }
}