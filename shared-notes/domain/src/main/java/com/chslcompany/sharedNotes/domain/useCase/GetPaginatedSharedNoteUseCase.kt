package com.chslcompany.sharedNotes.domain.useCase

import com.chslcompany.sharedNotes.domain.repository.SharedNoteRepository

class GetPaginatedSharedNoteUseCase(private val sharedNoteRepository: SharedNoteRepository) {

    suspend operator fun invoke() = sharedNoteRepository.getSharedNotes()
}