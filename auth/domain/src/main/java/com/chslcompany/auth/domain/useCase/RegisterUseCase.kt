package com.chslcompany.auth.domain.useCase

import com.chslcompany.auth.domain.repository.AuthRepository

class RegisterUseCase(private val authRepository: AuthRepository)  {
    operator fun invoke(email: String, password: String) = authRepository.register(email, password)
}