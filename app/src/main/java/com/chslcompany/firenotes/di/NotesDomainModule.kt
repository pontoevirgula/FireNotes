package com.chslcompany.firenotes.di

import com.chslcompany.notes.domain.repository.NotesRepository
import com.chslcompany.notes.domain.useCase.CreateNoteUseCase
import com.chslcompany.notes.domain.useCase.DeleteNoteUseCase
import com.chslcompany.notes.domain.useCase.GetAllNotesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object NotesDomainModule {

    @Provides
    fun providesCreateNoteUseCase(notesRepository: NotesRepository) : CreateNoteUseCase {
        return CreateNoteUseCase(notesRepository)
    }

    @Provides
    fun providesDeleteNoteUseCase(notesRepository: NotesRepository) : DeleteNoteUseCase {
        return DeleteNoteUseCase(notesRepository)
    }

    @Provides
    fun providesGetAllNotesUseCase(notesRepository: NotesRepository) : GetAllNotesUseCase {
        return GetAllNotesUseCase(notesRepository)
    }



}