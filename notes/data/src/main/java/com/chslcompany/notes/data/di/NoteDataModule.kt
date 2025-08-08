package com.chslcompany.notes.data.di

import com.chslcompany.notes.data.repository.NotesRepoImpl
import com.chslcompany.notes.domain.repository.NotesRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object NoteDataModule {


    @Provides
    @Singleton
    fun providesFirebaseFirestore() : FirebaseFirestore =
        FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseStorage() : FirebaseStorage =
        FirebaseStorage.getInstance()

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