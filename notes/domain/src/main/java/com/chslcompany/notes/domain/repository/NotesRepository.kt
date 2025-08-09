package com.chslcompany.notes.domain.repository

import com.chslcompany.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    fun createNote(imageUrl : String, note: Note) : Flow<Result<Unit>>

    fun updateNote(imageUrl : String, note: Note) : Flow<Result<Unit>>

    fun deleteNote(id : String) : Flow<Result<Unit>>

    fun getNotes(email : String) : Flow<List<Note>>

    fun getNote(id : String) : Flow<Result<Note>>

}