package com.chslcompany.firenotes.di

import com.chslcompany.sharedNotes.domain.repository.SharedNoteRepository
import com.chslcompany.sharedNotes.domain.useCase.GetPaginatedSharedNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object SharedNoteDomainModule {

    @Provides
    fun provideGetPaginatedSharedNoteUseCase(sharedNoteRepository: SharedNoteRepository) : GetPaginatedSharedNoteUseCase{
        return GetPaginatedSharedNoteUseCase(sharedNoteRepository)
    }
}