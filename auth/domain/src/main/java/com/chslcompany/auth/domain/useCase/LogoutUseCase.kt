package com.chslcompany.auth.domain.useCase

import com.chslcompany.auth.domain.repository.AuthRepository

class LogoutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() {
        authRepository.logout()
    }

}