package com.chslcompany.notes.domain.useCase

import com.chslcompany.notes.domain.repository.NotesRepository

class DeleteNoteUseCase(private val notesRepository: NotesRepository)  {
    operator fun invoke(id : String) = notesRepository.deleteNote(id)
}