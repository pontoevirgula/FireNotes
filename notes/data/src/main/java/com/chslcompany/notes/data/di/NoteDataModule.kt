package com.chslcompany.notes.data.di

import com.chslcompany.notes.data.repository.NotesRepoImpl
import com.chslcompany.notes.domain.repository.NotesRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
@InstallIn(SingletonComponent::class)
@Module
object NoteDataModule {

    @Provides
    fun providesNotesRepoImpl(
        firebaseStorage: FirebaseStorage,
        firebaseFirestore: FirebaseFirestore
    ) : NotesRepository {
        return NotesRepoImpl(
            firebaseStorage, firebaseFirestore
        )
    }

}