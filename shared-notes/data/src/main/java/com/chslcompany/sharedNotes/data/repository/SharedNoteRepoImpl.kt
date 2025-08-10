package com.chslcompany.sharedNotes.data.repository

import com.chslcompany.sharedNotes.domain.model.SharedNote
import com.chslcompany.sharedNotes.domain.repository.SharedNoteRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SharedNoteRepoImpl(
    private val firebaseFirestore: FirebaseFirestore
): SharedNoteRepository  {

    private val notesCollection by lazy { firebaseFirestore.collection("notes") }

    private var documentsSnapshot : DocumentSnapshot? = null
    private var _isLoading: Boolean = false
    private var _endPagination : Boolean = false
    private var pageSize = 3

    override suspend fun getSharedNotes(): List<SharedNote> {
        if (_isLoading || _endPagination) return emptyList()

        _isLoading = true

        var query = notesCollection.whereEqualTo("shared", true)
            .limit(pageSize.toLong())

        documentsSnapshot?.let {
            query = query.startAfter(it)
        }

        try {
            val querySnapshot = query.get().await()
            val notes = querySnapshot.toObjects(SharedNote::class.java)
            if (querySnapshot.isEmpty.not()){
                documentsSnapshot = querySnapshot.documents.last()
            } else {
                _endPagination = true
            }
            return notes
        }finally {
            _isLoading = false
        }



    }
}