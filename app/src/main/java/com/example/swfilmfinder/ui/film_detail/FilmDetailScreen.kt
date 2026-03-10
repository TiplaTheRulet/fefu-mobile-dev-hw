package com.example.swfilmfinder.ui.film_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.swfilmfinder.ui.common.ErrorScreen
import com.example.swfilmfinder.ui.common.LoadingScreen
import com.example.swfilmfinder.ui.common.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDetailScreen(
    viewModel: FilmDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали фильма") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val s = state) {
                is UiState.Loading -> LoadingScreen()
                is UiState.Error -> ErrorScreen(message = s.message, onRetry = { viewModel.loadDetail() })
                is UiState.Success -> {
                    val film = s.data
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(text = film.title, style = MaterialTheme.typography.headlineLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Дата выхода: ${film.releaseDate}")
                        Text(text = "Режиссёр: ${film.director}")

                        Card(
                            modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Вычисляемые поля", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Возраст фильма: ${film.yearsSinceRelease} лет")
                                Text(text = "Кол-во персонажей: ${film.characterCount}")
                                Text(text = "Кол-во планет: ${film.planetsCount}")
                                Text(text = "Кол-во космических кораблей: ${film.starshipsCount}")
                                Text(text = "Кол-во техники: ${film.vehiclesCount}")
                                Text(text = "Кол-во рас: ${film.speciesCount}")
                            }
                        }

                        Text(text = "Вступительные титры:", style = MaterialTheme.typography.titleLarge)
                        Text(text = film.openingCrawl, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
