package com.chslcompany.auth.domain.useCase

import com.chslcompany.auth.domain.repository.AuthRepository

class GetCurrentUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(){
        authRepository.getCurrentUser()
    }

}