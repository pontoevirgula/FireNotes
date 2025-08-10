package com.chslcompany.sharedNotes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chslcompany.sharedNotes.domain.model.SharedNote
import com.chslcompany.sharedNotes.domain.useCase.GetPaginatedSharedNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SharedNoteViewModel @Inject constructor(
    private val getPaginatedSharedNoteUseCase: GetPaginatedSharedNoteUseCase
) : ViewModel() {

    private val _sharedNotes = MutableStateFlow<List<SharedNote>>(emptyList())
    val sharedNotes = _sharedNotes.asStateFlow()

    init {
       getPaginatedNotes()
    }

    fun getPaginatedNotes() = viewModelScope.launch {
        val sharedNotes = getPaginatedSharedNoteUseCase()
        _sharedNotes.update { it + sharedNotes }
    }
}