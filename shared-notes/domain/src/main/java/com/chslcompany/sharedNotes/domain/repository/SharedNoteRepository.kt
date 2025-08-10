package com.chslcompany.sharedNotes.domain.repository

import com.chslcompany.sharedNotes.domain.model.SharedNote

interface SharedNoteRepository {

    suspend fun getSharedNotes() : List<SharedNote>
}