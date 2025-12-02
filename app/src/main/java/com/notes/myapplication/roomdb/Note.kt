package com.notes.myapplication.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = false) val title: String,
    val body: String,
    val date: String,
    val tags: List<String>
)