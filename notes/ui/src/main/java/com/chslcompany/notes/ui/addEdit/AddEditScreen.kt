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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
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

@Composable
fun AddEditScreen(modifier: Modifier = Modifier, popBackStack: () -> Unit) {
    val viewModel = hiltViewModel<AddEditViewModel>()

    val title by viewModel.title.collectAsStateWithLifecycle()
    val content by viewModel.content.collectAsStateWithLifecycle()
    val imageUrl by viewModel.imageUrl.collectAsStateWithLifecycle()
    val shared by viewModel.shared.collectAsStateWithLifecycle()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState.isPopBackStack){
            popBackStack()
        }
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.onImageUrlChange(uri.toString())
            }
        }
    )

    AddEditScreenContent(
        modifier = modifier.fillMaxSize(),
        title = title,
        content = content,
        shared = shared,
        imageUrl = imageUrl,
        onTitleChange = viewModel::onTitleChange,
        onContentChange = viewModel::onContentChange,
        onSharedChange = viewModel::onSharedChange,
        addImage = { photoPicker.launch(PickVisualMediaRequest()) },
        createNote = viewModel::createNote

    )

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreenContent(
    modifier: Modifier = Modifier,
    title : String,
    content : String,
    shared : Boolean,
    imageUrl : String,
    onTitleChange : (String) -> Unit,
    onContentChange : (String) -> Unit,
    onSharedChange : (Boolean) -> Unit,
    addImage : () -> Unit,
    createNote: () -> Unit
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Adicionar/Editar") })
        }, floatingActionButton = {
            FloatingActionButton(onClick = createNote){
               Icon(imageVector = Icons.Default.Check, contentDescription = null)
            }
        }) { innerPadding ->

        Column(Modifier
            .padding(innerPadding)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ) {
            Text("Título")
            Spacer(modifier = Modifier.height(height = 8.dp))
            TextField(
                value = title, onValueChange = onTitleChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Digite o título de sua tarefa") })

            Spacer(modifier = Modifier.height(height = 12.dp))

            Text("Conteúdo")
            Spacer(modifier = Modifier.height(height = 8.dp))
            TextField(
                value = content, onValueChange = onContentChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Digite o conteúdo de sua tarefa") }
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

            if(imageUrl.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable{
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
            } else{
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Inside
                )
        }

    }


    }
}