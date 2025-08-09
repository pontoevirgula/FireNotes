package com.chslcompany.notes.domain.useCase

import com.chslcompany.notes.domain.repository.NotesRepository

class GetNoteUseCase(private val notesRepository: NotesRepository)  {
    operator fun invoke(id: String) = notesRepository.getNote(id)
}