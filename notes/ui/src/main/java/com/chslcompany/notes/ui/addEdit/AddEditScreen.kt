package com.chslcompany.notes.ui.addEdit

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.alpha

@Composable
fun AddEditScreen(
    modifier: Modifier = Modifier,
    id: String?,
    popBackStack: () -> Unit
) {
    val viewModel = hiltViewModel<AddEditViewModel>()

    val title by viewModel.title.collectAsStateWithLifecycle()
    val content by viewModel.content.collectAsStateWithLifecycle()
    val imageUrl by viewModel.imageUrl.collectAsStateWithLifecycle()
    val shared by viewModel.shared.collectAsStateWithLifecycle()

    val isEdit by viewModel.isEdit.collectAsStateWithLifecycle()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        if (id != null){
            viewModel.getNote(id)
        }
    }

    LaunchedEffect(uiState) {
        if (uiState.isPopBackStack) {
            popBackStack()
        }
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri == null) return@rememberLauncherForActivityResult
            viewModel.onImageUrlChange(uri.toString())

        }
    )

    AddEditScreenContent(
        modifier = modifier.fillMaxSize(),
        isEdit = isEdit,
        title = title,
        content = content,
        shared = shared,
        imageUrl = imageUrl,
        isImageLoading = uiState.isImageLoading,
        onTitleChange = viewModel::onTitleChange,
        onContentChange = viewModel::onContentChange,
        onSharedChange = viewModel::onSharedChange,
        addImage = { photoPicker.launch(PickVisualMediaRequest()) },
        createNote = viewModel::createNote,
        updateNote = viewModel::updateNote,
        removeImage = {
            viewModel.onImageUrlChange("")
        }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreenContent(
    modifier: Modifier = Modifier,
    isEdit: Boolean = false,
    title: String,
    content: String,
    shared: Boolean,
    imageUrl: String,
    isImageLoading: Boolean,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onSharedChange: (Boolean) -> Unit,
    addImage: () -> Unit,
    createNote: () -> Unit,
    updateNote: () -> Unit,
    removeImage: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Adicionar/Editar") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (!isImageLoading) {
                        if(isEdit)
                            updateNote()
                        else
                            createNote()
                    }
                },
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
                    .then(
                        if (isImageLoading) {
                            Modifier.alpha(0.6f)
                        } else {
                            Modifier
                        }
                    )
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Salvar anotação")
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .navigationBarsPadding()
        ) {
            Text("Título")
            Spacer(modifier = Modifier.height(height = 8.dp))
            TextField(
                value = title,
                onValueChange = onTitleChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Digite o título de sua tarefa") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(height = 12.dp))

            Text("Conteúdo")
            Spacer(modifier = Modifier.height(height = 8.dp))
            TextField(
                value = content,
                onValueChange = onContentChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Digite o conteúdo de sua tarefa") },
                minLines = 3,
                maxLines = 8
            )

            Spacer(modifier = Modifier.height(height = 12.dp))

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Compartilhar")
                Spacer(modifier = Modifier.height(height = 8.dp))
                Checkbox(checked = shared, onCheckedChange = onSharedChange)
            }

            Spacer(modifier = Modifier.height(height = 12.dp))

            if (imageUrl.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            addImage()
                        }
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = Icons.Default.Add, contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Inside
                    )
                    
                    if (isImageLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(height = 12.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    onClick = (removeImage),
                    enabled = !isImageLoading
                ){
                    Text("Excluir imagem")
                }
            }
        }
    }
}