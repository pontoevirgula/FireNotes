package com.chslcompany.notes.data.repository

import androidx.core.net.toUri
import com.chslcompany.notes.domain.model.Note
import com.chslcompany.notes.domain.repository.NotesRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class NotesRepoImpl(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore
) : NotesRepository {
    companion object{
        private const val NOTES = "notes"
    }

    private val notesCollection by lazy { firebaseFirestore.collection(NOTES) }
    private val imageCollection by lazy { firebaseStorage.getReference(NOTES) }

    override fun createNote(imageUrl : String,note: Note): Flow<Result<Unit>> {
        return flow {
            try {
                if (imageUrl.isNotEmpty()) {
                    val imageRef = imageCollection.child(note.id)
                    imageRef.putFile(imageUrl.toUri()).await()
                    val uploadedImageUrl = imageRef.downloadUrl.await().toString()
                    val updateNote = note.copy(imageUrl = uploadedImageUrl)
                    notesCollection.document(note.id).set(updateNote).await()
                } else {
                    notesCollection.document(note.id).set(note).await()
                }
                emit(Result.success(Unit))
            }catch (e: Exception){
                emit(Result.failure(e))
            }
        }
    }

    override fun updateNote(
        imageUrl: String,
        note: Note
    ): Flow<Result<Unit>> {
        return callbackFlow {
            if (imageUrl == note.imageUrl){
                notesCollection
                    .whereEqualTo("id", note.id)
                    .limit(1)
                    .get()
                    .await()
                    .documents
                    .firstOrNull()
                    ?.reference?.set(note, SetOptions.merge())
                    ?.await()
                trySend(Result.success(Unit))
            } else{
                if (imageUrl.isNotEmpty()){
                    val imageRef = imageCollection.child(note.id)
                    imageRef.putFile(imageUrl.toUri()).await()
                    val uploadedImageUrl = imageRef.downloadUrl.await().toString()
                    val updateNote = note.copy(imageUrl = uploadedImageUrl)
                    notesCollection
                        .whereEqualTo("id", note.id)
                        .limit(1)
                        .get()
                        .await()
                        .documents
                        .firstOrNull()
                        ?.reference?.set(updateNote, SetOptions.merge())
                        ?.await()
                    trySend(Result.success(Unit))
                } else {
                    val imageRef = imageCollection.child(note.id)
                    imageRef.delete().await()
                    val updateNote = note.copy(imageUrl = "")
                    notesCollection
                        .whereEqualTo("id", note.id)
                        .limit(1)
                        .get()
                        .await()
                        .documents
                        .firstOrNull()
                        ?.reference?.set(updateNote, SetOptions.merge())
                        ?.await()
                    trySend(Result.success(Unit))
                }
            }
            awaitClose{ }
        }
    }

    override fun deleteNote(id: String): Flow<Result<Unit>> {
        return flow {
            try {
                val querySnapshot = notesCollection.whereEqualTo("id", id)
                    .limit(1)
                    .get()
                    .await()

                querySnapshot.documents.forEach { document ->
                    val imageUrl = document.getString("imageUrl")
                    if (imageUrl.isNullOrBlank().not()) {
                        imageCollection.child(id).delete().await()
                    }
                    document.reference.delete().await()
                }
                emit(Result.success(Unit))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }

    override fun getNotes(email: String): Flow<List<Note>> {
       return callbackFlow {
           if (email.isEmpty()) {
               trySend(emptyList())
               awaitClose { }
               return@callbackFlow
           }

           val listener = notesCollection
               .whereEqualTo("email", email)
               .addSnapshotListener { snapshot, error ->
                  if (error != null) {
                      return@addSnapshotListener
                  }

                  val notes = snapshot?.toObjects(Note::class.java) ?: emptyList()
                  trySend(notes)
               }
           awaitClose { listener.remove() }
       }
    }

    override fun getNote(id: String): Flow<Result<Note>> {
        return callbackFlow {

            val note = notesCollection
                .whereEqualTo("id", id)
                .limit(1)
                .get()
                .await()
                .toObjects(Note::class.java)
                .firstOrNull()

            if (note == null) trySend(Result.failure(Exception("Anotação não encontrada")))
            else trySend(Result.success(note))

            awaitClose{}
        }
    }
}