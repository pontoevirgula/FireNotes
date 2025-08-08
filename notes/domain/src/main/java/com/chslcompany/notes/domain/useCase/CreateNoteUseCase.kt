package com.chslcompany.notes.domain.useCase

import com.chslcompany.notes.domain.model.Note
import com.chslcompany.notes.domain.repository.NotesRepository

class CreateNoteUseCase(private val notesRepository: NotesRepository)  {
    operator fun invoke(imageUrl : String, note: Note) =
        notesRepository.createNote(imageUrl, note)

}