package com.chslcompany.sharedNotes.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.chslcompany.sharedNotes.domain.model.SharedNote
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SharedNoteScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<SharedNoteViewModel>()
    val sharedNotes by viewModel.sharedNotes.collectAsStateWithLifecycle(emptyList())
    SharedNoteScreenContent(
        modifier = modifier.fillMaxSize(),
        list = sharedNotes,
        loadNextPage = viewModel::getPaginatedNotes
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedNoteScreenContent(
    modifier: Modifier = Modifier,
    list: List<SharedNote>,
    loadNextPage: () -> Unit,
) {
    val lazyListState = rememberLazyListState()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Anotações compartilhadas") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, contentDescription = null,
                        modifier = Modifier.clickable {

                        })
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            items(
                list.size,
                key = { list[it].id },
                contentType = { list[it].id }) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    val item = list.get(index)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp, bottom = 12.dp)
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(Color.Black)
                                    .clip(CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(item.email.first().uppercase(), color = Color.White)
                            }
                            Spacer(Modifier.width(8.dp))
                            Text(item.email, style = MaterialTheme.typography.titleMedium)
                        }

                        SubcomposeAsyncImage(
                            model = item.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .fillMaxWidth()
                                .height(250.dp),
                            loading = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(250.dp), contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }, error = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(250.dp), contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        item.email.first().uppercase(),
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(item.title, style = MaterialTheme.typography.titleLarge
                            .copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(item.content, style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 12.dp)
                                .padding(top = 8.dp, bottom = 12.dp))
                    }
                }

            }
        }
        val shouldTriggerPagination = remember {
            derivedStateOf {
                val lastVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()
                val totalItems = lazyListState.layoutInfo.totalItemsCount
                lastVisibleItem != null && lastVisibleItem.index >= totalItems - 1
            }
        }

        LaunchedEffect(shouldTriggerPagination) {
            if (shouldTriggerPagination.value) {
                loadNextPage()
            }
        }

    }
}