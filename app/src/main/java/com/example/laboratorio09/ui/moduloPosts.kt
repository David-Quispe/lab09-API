package com.example.laboratorio09.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun ScreenPosts(navController: NavHostController, viewModel: PostsViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadPosts() }

    when {
        state.isLoading -> LoadingMessage()
        state.error != null -> ErrorMessage(state.error.orEmpty(), viewModel::loadPosts)
        else -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(Modifier.padding(bottom = 10.dp)) {
                    Text(
                        "Publicaciones",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "${state.posts.size} lecturas para explorar",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            items(state.posts, key = { it.id }) { post ->
                Card(
                    onClick = { navController.navigate("postsVer/${post.id}") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "N.º ${post.id}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                                )
                            }
                            Text(
                                "Usuario ${post.userId}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(Modifier.height(14.dp))
                        Text(
                            post.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            post.body,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(14.dp))
                        Text(
                            "Leer publicación",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScreenPost(
    navController: NavHostController,
    id: Int,
    viewModel: PostsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(id) { viewModel.loadPost(id) }

    when {
        state.isLoading -> LoadingMessage()
        state.error != null -> ErrorMessage(state.error.orEmpty()) { viewModel.loadPost(id) }
        state.selectedPost != null -> {
            val post = state.selectedPost ?: return
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(Modifier.fillMaxWidth().padding(22.dp)) {
                        Text(
                            "Publicación ${post.id} · Usuario ${post.userId}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(Modifier.height(14.dp))
                        Text(
                            post.title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(Modifier.fillMaxWidth().padding(22.dp)) {
                        Text(
                            "Contenido",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            post.body,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                OutlinedButton(onClick = { navController.popBackStack() }) {
                    Text("Volver a publicaciones")
                }
            }
        }
    }
}

@Composable
private fun LoadingMessage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Text("Cargando publicaciones…", modifier = Modifier.padding(top = 12.dp))
    }
}

@Composable
private fun ErrorMessage(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No pudimos cargar las publicaciones", style = MaterialTheme.typography.titleMedium)
        Text(message, modifier = Modifier.padding(vertical = 12.dp))
        Button(onClick = onRetry) { Text("Reintentar") }
    }
}
