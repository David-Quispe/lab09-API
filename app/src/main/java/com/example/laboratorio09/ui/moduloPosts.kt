package com.example.laboratorio09.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.posts, key = { it.id }) { post ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .clickable { navController.navigate("postsVer/${post.id}") }
                ) {
                    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("${post.id}.", style = MaterialTheme.typography.titleMedium)
                        Text(post.title, style = MaterialTheme.typography.bodyLarge)
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
                modifier = Modifier.fillMaxSize().padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Post ${post.id}", style = MaterialTheme.typography.headlineSmall)
                Text("Usuario ${post.userId}", style = MaterialTheme.typography.labelLarge)
                Text(post.title, style = MaterialTheme.typography.titleLarge)
                Text(post.body, style = MaterialTheme.typography.bodyLarge)
                Button(onClick = { navController.popBackStack() }) { Text("Volver a posts") }
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
        Text("Cargando posts…", modifier = Modifier.padding(top = 12.dp))
    }
}

@Composable
private fun ErrorMessage(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Error de conexión", style = MaterialTheme.typography.titleMedium)
        Text(message, modifier = Modifier.padding(vertical = 12.dp))
        Button(onClick = onRetry) { Text("Reintentar") }
    }
}
