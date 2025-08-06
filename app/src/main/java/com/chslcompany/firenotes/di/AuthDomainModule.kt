package com.chslcompany.firenotes.di

import com.chslcompany.auth.domain.repository.AuthRepository
import com.chslcompany.auth.domain.useCase.GetCurrentUserUseCase
import com.chslcompany.auth.domain.useCase.LoginUseCase
import com.chslcompany.auth.domain.useCase.LogoutUseCase
import com.chslcompany.auth.domain.useCase.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AuthDomainModule {
    @Provides
    fun providesGetCurrentUserUseCase(authRepository: AuthRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(authRepository)
    }

    @Provides
    fun providesLogoutUseCase(authRepository: AuthRepository): LogoutUseCase {
        return LogoutUseCase(authRepository)
    }

    @Provides
    fun providesLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    fun providesRegisterUseCase(authRepository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(authRepository)
    }

}