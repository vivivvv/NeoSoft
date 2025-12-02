package com.notes.myapplication.di

import android.content.Context
import androidx.room.Room
import com.notes.myapplication.roomdb.NoteDao
import com.notes.myapplication.roomdb.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): NoteDatabase =
        Room.databaseBuilder(
            appContext,
            NoteDatabase::class.java,
            "note_database"
        ).build()

    @Provides
    fun provideNoteDao(database: NoteDatabase): NoteDao = database.noteDao()
}
