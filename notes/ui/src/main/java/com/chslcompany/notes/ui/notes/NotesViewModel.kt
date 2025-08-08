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

    init {
        viewModelScope.launch {
            _user.update { getCurrentUserUseCase() }
        }
        getNotes()
    }

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes = _notes.asStateFlow()

    fun getNotes() {
        getAllNotesUseCase(_user.value?.email.orEmpty())
            .onEach { result ->
                _notes.update { result }
            }.launchIn(viewModelScope)
    }

    fun deleteNote(id : String){
        deleteNoteUseCase(id).onEach { result ->
            result.onSuccess {}
                .onFailure {  }
        }.launchIn(viewModelScope)

    }

}