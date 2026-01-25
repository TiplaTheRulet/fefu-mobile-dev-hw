package com.example.pokedex.presentation.pokemonlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    var active by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(searchText) {
        viewModel.onEvent(PokemonListEvent.Search(searchText))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PokeDex") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                query = searchText,
                onQueryChange = { searchText = it },
                onSearch = { active = false },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text("Search Pokemon by name...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                searchText = ""
                                viewModel.onEvent(PokemonListEvent.ClearSearch)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                }
            ) {
            }

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when (val currentState = state) {
                    is PokemonListState.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Text(
                                text = "Loading Pokemon...",
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    }

                    is PokemonListState.Empty -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (searchText.isNotEmpty()) {
                                Text(
                                    text = "No Pokemon found for \"$searchText\"",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Button(
                                    onClick = {
                                        searchText = ""
                                        viewModel.onEvent(PokemonListEvent.ClearSearch)
                                    },
                                    modifier = Modifier.padding(top = 16.dp)
                                ) {
                                    Text("Clear search")
                                }
                            } else {
                                Text(
                                    text = "No Pokemon found",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Button(
                                    onClick = { viewModel.onEvent(PokemonListEvent.Retry) },
                                    modifier = Modifier.padding(top = 16.dp)
                                ) {
                                    Text("Retry")
                                }
                            }
                        }
                    }

                    is PokemonListState.Error -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = currentState.message,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                            Button(
                                onClick = { viewModel.onEvent(PokemonListEvent.Retry) },
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Text("Retry")
                            }
                        }
                    }

                    is PokemonListState.Success -> {
                        val displayText = if (searchText.isNotEmpty()) {
                            "Found ${currentState.filteredPokemons.size} Pokemon for \"$searchText\""
                        } else {
                            "Showing ${currentState.filteredPokemons.size} Pokemon"
                        }

                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = displayText,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            )

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                items(currentState.filteredPokemons) { pokemon ->
                                    PokemonListItem(
                                        pokemon = pokemon,
                                        isFavorite = favorites.contains(pokemon.id),
                                        onFavoriteClick = {
                                            viewModel.onEvent(PokemonListEvent.ToggleFavorite(pokemon.id))
                                        },
                                        onClick = {
                                            navController.navigate("pokemon_detail/${pokemon.id}")
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}