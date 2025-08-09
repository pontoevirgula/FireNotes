package com.chslcompany.notes.ui.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.chslcompany.notes.domain.model.Note
import androidx.compose.runtime.getValue

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    goToAddEditNoteScreen : (String?) -> Unit
) {

    val viewModel = hiltViewModel<NotesViewModel>()

    val notes by viewModel.notes.collectAsStateWithLifecycle()



    NotesScreenContent(
        modifier = modifier,
        notes = notes,
        onDelete = viewModel::deleteNote,
        onAddNote = { goToAddEditNoteScreen(null) }
    )
}


@Composable
fun NotesScreenContent(
    modifier: Modifier = Modifier, 
    notes: List<Note>, 
    onDelete: (String) -> Unit,
    onAddNote: () -> Unit
){
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNote
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar nota"
                )
            }
        }
    ) {
        if (notes.isEmpty()) {
            // Show empty state
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Nenhuma nota encontrada",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Adicione sua primeira nota!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                androidx.compose.material3.Button(
                    onClick = onAddNote
                ) {
                    Text("Adicionar Nota")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                items (
                    notes.size,
                    key = { index -> notes.get(index).id },
                    contentType = { index -> notes.get(index).id }
                ) { index ->
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .fillMaxWidth()
                            .background(
                                color = if(notes.get(index).shared) Color.Green else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        AsyncImage(
                            model = notes.get(index).imageUrl, contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .background(color = Color.White, shape = CircleShape)
                                .clip(CircleShape)
                       )

                        Column(modifier = Modifier.weight(1f)){
                            Text(notes.get(index).title, style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.height(8.dp))
                            Text(notes.get(index).content, style = MaterialTheme.typography.bodyMedium)
                        }

                        IconButton(
                            onClick = { onDelete.invoke(notes.get(index).id) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete, contentDescription = null,
                                tint = Color.Red
                            )
                        }

                    }
                }
            }
        }
    }
}

