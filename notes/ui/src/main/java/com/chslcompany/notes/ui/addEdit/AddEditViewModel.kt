package com.chslcompany.notes.ui.addEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chslcompany.auth.domain.model.User
import com.chslcompany.auth.domain.useCase.GetCurrentUserUseCase
import com.chslcompany.notes.domain.model.Note
import com.chslcompany.notes.domain.useCase.CreateNoteUseCase
import com.chslcompany.notes.domain.useCase.GetNoteUseCase
import com.chslcompany.notes.domain.useCase.GetUpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.util.UUID


@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val createNoteUseCase: CreateNoteUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUpdateNoteUseCase: GetUpdateNoteUseCase,
    private val getNoteUseCase: GetNoteUseCase

): ViewModel() {

    private val _editNote = MutableStateFlow<Note?>(null)

    private val _isEdit = MutableStateFlow(false)
    val isEdit = _isEdit.asStateFlow()

    private val _uiState = MutableStateFlow(AddEditUiState())
    val uiState = _uiState.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _content = MutableStateFlow("")
    val content = _content.asStateFlow()

    private val _imageUrl = MutableStateFlow("")
    val imageUrl = _imageUrl.asStateFlow()

    private val _shared = MutableStateFlow(false)
    val shared = _shared.asStateFlow()

    fun onTitleChange(title: String) {
        _title.update { title }
    }

    fun onContentChange(content: String) {
        _content.update { content }
    }

    fun onImageUrlChange(imageUrl: String) {
        _imageUrl.update { imageUrl }
    }

    fun onSharedChange(shared: Boolean) {
        _shared.update { shared }
    }

    init {
        viewModelScope.launch {
            _user.update { getCurrentUserUseCase() }
        }
    }


    fun createNote() {
        val note = Note(
            id = UUID.randomUUID().toString(),
            email = _user.value?.email.orEmpty() ,
            title = title.value,
            content = content.value,
            imageUrl = imageUrl.value,
            shared = shared.value,
        )
        
        // Se há imageUrl, mostrar loading de imagem imediatamente
        if (_imageUrl.value.isNotEmpty()) {
            _uiState.update { it.copy(isImageLoading = true) }
        }
        
        createNoteUseCase(_imageUrl.value, note)
            .onStart{ 
                _uiState.update { AddEditUiState(isImageLoading = _imageUrl.value.isNotEmpty()) }
            }
            .onEach { result ->
                result.onSuccess {

                    // Se há imageUrl, continuar o loading por 1 segundo
                    if (_imageUrl.value.isNotEmpty()) {
                        viewModelScope.launch {
                            delay(1000) // Delay de 1 segundo
                            _uiState.update { it.copy(isImageLoading = false, isPopBackStack = true) }
                        }
                    } else {
                        // Se não há imageUrl, navegar imediatamente
                        _uiState.update { it.copy(isPopBackStack = true) }
                    }
                }.onFailure { error ->
                    _uiState.update { AddEditUiState(isImageLoading = false) }
                }

            }.launchIn(viewModelScope)
    }

    fun getNote(id: String) {
        _isEdit.update { true }
        getNoteUseCase(id)
            .onEach { result ->
                result.onSuccess {data ->
                    _editNote.update { data }
                    _title.update { data.title }
                    _content.update { data.content }
                    _imageUrl.update { data.imageUrl }
                    _shared.update { data.shared }
                }.onFailure { error ->

                }
            }.onCompletion {
                _uiState.update { AddEditUiState() }
            }.launchIn(viewModelScope)
    }

    fun updateNote() {
        val note = Note(
            id = _editNote.value?.id.orEmpty(),
            email = _user.value?.email.orEmpty() ,
            title = _title.value,
            content = _content.value,
            imageUrl = _editNote.value?.imageUrl.orEmpty(),
            shared = _shared.value,
        )
        
        // Se há imageUrl, mostrar loading de imagem imediatamente
        if (_imageUrl.value.isNotEmpty()) {
            _uiState.update { it.copy(isImageLoading = true) }
        }
        
        getUpdateNoteUseCase(_imageUrl.value, note)
            .onStart{
                _uiState.update { AddEditUiState(isImageLoading = _imageUrl.value.isNotEmpty()) }
            }
            .onEach { result ->
                result.onSuccess { data ->
                    // Se há imageUrl, continuar o loading por 1 segundo
                    if (_imageUrl.value.isNotEmpty()) {
                        viewModelScope.launch {
                            delay(1000) // Delay de 1 segundo
                            _uiState.update { it.copy(isImageLoading = false, isPopBackStack = true) }
                        }
                    } else {
                        // Se não há imageUrl, navegar imediatamente
                        _uiState.update { it.copy(isPopBackStack = true) }
                    }
                }.onFailure { error -> 
                    _uiState.update { it.copy(isImageLoading = false) }
                }
            }.onCompletion {
                _uiState.update { AddEditUiState() }
            }.launchIn(viewModelScope)
    }


}