package com.chslcompany.auth.data.repository

import com.chslcompany.auth.domain.model.User
import com.chslcompany.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override fun login(
        email: String,
        password: String
    ): Flow<Result<User>> {
        return callbackFlow {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { firebaseUser ->
                    firebaseUser.user?.let {
                        trySend(
                            Result.success(
                                User(
                                    uid = it.uid,
                                    email = it.email.orEmpty(),
                                    displayName = it.displayName.orEmpty()
                                )
                            )
                        )
                    }
                }.addOnFailureListener { error ->
                    trySend(Result.failure(error))
                }

            awaitClose {}
        }
    }

    override fun register(
        email: String,
        password: String
    ): Flow<Result<User>> {
        return callbackFlow {
            firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { firebaseUser ->
                    firebaseUser.user?.let {
                        trySend(
                            Result.success(
                                User(
                                    uid = it.uid,
                                    email = it.email.orEmpty(),
                                    displayName = it.displayName.orEmpty()
                                )
                            )
                        )
                    }
                }
                .addOnFailureListener { error ->
                    trySend(Result.failure(error))
                }
            awaitClose {}
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.let {
            User(
                uid = it.uid,
                email = it.email.orEmpty(),
                displayName = it.displayName.orEmpty()
            )
        }
    }
}