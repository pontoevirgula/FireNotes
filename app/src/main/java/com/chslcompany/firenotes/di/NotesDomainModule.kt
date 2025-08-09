package com.chslcompany.firenotes.di

import com.chslcompany.notes.domain.repository.NotesRepository
import com.chslcompany.notes.domain.useCase.CreateNoteUseCase
import com.chslcompany.notes.domain.useCase.DeleteNoteUseCase
import com.chslcompany.notes.domain.useCase.GetAllNotesUseCase
import com.chslcompany.notes.domain.useCase.GetNoteUseCase
import com.chslcompany.notes.domain.useCase.GetUpdateNoteUseCase
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

    @Provides
    fun providesGetNoteUseCase(notesRepository: NotesRepository) : GetNoteUseCase {
        return GetNoteUseCase(notesRepository)
    }

    @Provides
    fun providesGetUpdateNoteUseCase(notesRepository: NotesRepository) : GetUpdateNoteUseCase {
        return GetUpdateNoteUseCase(notesRepository)
    }



}