package com.chslcompany.notes.ui.addEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chslcompany.auth.domain.model.User
import com.chslcompany.auth.domain.useCase.GetCurrentUserUseCase
import com.chslcompany.notes.domain.model.Note
import com.chslcompany.notes.domain.useCase.CreateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID


@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val createNoteUseCase: CreateNoteUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): ViewModel() {

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
        
        createNoteUseCase(_imageUrl.value, note)
            .onStart{ 
                _uiState.update { AddEditUiState(isLoading = true) }
            }
            .onEach { result ->
                result.onSuccess {
                    _uiState.update { AddEditUiState(isLoading = false, isPopBackStack = true) }
                }.onFailure { error ->
                    _uiState.update { AddEditUiState(isLoading = false) }
                }

            }.launchIn(viewModelScope)
    }



}