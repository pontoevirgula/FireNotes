package com.chslcompany.auth.domain.useCase

import com.chslcompany.auth.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository)  {
    operator fun invoke(email: String, password: String) = authRepository.login(email, password)

}