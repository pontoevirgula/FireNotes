package com.chslcompany.sharedNotes.data.repository

import com.chslcompany.sharedNotes.domain.repository.SharedNoteRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object SharedNotesDataModule {


    @Provides
    fun provideSharedNoteRepository(firebaseFirestore: FirebaseFirestore) : SharedNoteRepository{
        return SharedNoteRepoImpl(firebaseFirestore)

    }
}