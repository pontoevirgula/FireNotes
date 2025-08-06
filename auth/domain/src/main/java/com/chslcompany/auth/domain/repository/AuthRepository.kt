package com.chslcompany.auth.domain.repository

import com.chslcompany.auth.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun login(email: String, password: String): Flow<Result<User>>

    fun register(email: String, password: String): Flow<Result<User>>

    suspend fun logout()

    suspend fun getCurrentUser(): User?
}