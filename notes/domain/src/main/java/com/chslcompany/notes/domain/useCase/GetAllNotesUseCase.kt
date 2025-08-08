package com.chslcompany.notes.domain.useCase

import com.chslcompany.notes.domain.repository.NotesRepository

class GetAllNotesUseCase(private val notesRepository: NotesRepository)  {
    operator fun invoke(email : String) =
        notesRepository.getNotes(email)

}