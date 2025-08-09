package com.chslcompany.auth.data.di

import com.chslcompany.auth.data.repository.AuthRepositoryImpl
import com.chslcompany.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AuthDataModule {

    @Provides
    fun providesAuthRepository(auth : FirebaseAuth) : AuthRepository {
        return AuthRepositoryImpl(auth)
    }

}