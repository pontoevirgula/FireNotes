package com.chslcompany.notes.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chslcompany.auth.domain.model.User
import com.chslcompany.auth.domain.useCase.GetCurrentUserUseCase
import com.chslcompany.notes.domain.model.Note
import com.chslcompany.notes.domain.useCase.DeleteNoteUseCase
import com.chslcompany.notes.domain.useCase.GetAllNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
): ViewModel() {

    private val _user = MutableStateFlow<User?>(value = null)

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes = _notes.asStateFlow()

    init {
        viewModelScope.launch {
            val user = getCurrentUserUseCase()
            _user.update { user }
            // Get notes after user is loaded
            if (user != null) {
                getNotes()
            }
        }
    }

    fun getNotes() {
        val userEmail = _user.value?.email.orEmpty()
        if (userEmail.isNotEmpty()) {
            getAllNotesUseCase(userEmail)
                .onEach { result ->
                    _notes.update { result }
                }.launchIn(viewModelScope)
        } else {
            _notes.update { emptyList() }
        }
    }

    fun deleteNote(id: String) {
        deleteNoteUseCase(id).onEach { result ->
            result.onSuccess {
                // Refresh notes after successful deletion
                getNotes()
            }.onFailure { 
                // Handle error if needed
            }
        }.launchIn(viewModelScope)
    }

}