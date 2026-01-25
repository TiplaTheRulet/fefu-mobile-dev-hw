package com.example.pokedex.presentation.pokemonlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.model.Pokemon
import com.example.pokedex.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PokemonListState {
    object Loading : PokemonListState()
    object Empty : PokemonListState()
    data class Error(val message: String) : PokemonListState()
    data class Success(
        val allPokemons: List<Pokemon>,
        val filteredPokemons: List<Pokemon>
    ) : PokemonListState()
}

class PokemonListViewModel : ViewModel() {
    private val repository = PokemonRepository()
    private val _state = MutableStateFlow<PokemonListState>(PokemonListState.Loading)
    val state: StateFlow<PokemonListState> = _state.asStateFlow()

    private val _favorites = MutableStateFlow<Set<Int>>(emptySet())
    val favorites: StateFlow<Set<Int>> = _favorites.asStateFlow()

    var searchQuery by mutableStateOf("")
        private set

    private var allPokemons: List<Pokemon> = emptyList()

    init {
        loadPokemons()
    }

    fun loadPokemons() {
        viewModelScope.launch {
            _state.value = PokemonListState.Loading
            try {
                val response = repository.getPokemonList(limit = 999)
                allPokemons = response.results
                if (allPokemons.isEmpty()) {
                    _state.value = PokemonListState.Empty
                } else {
                    applySearchFilter(searchQuery)
                }
            } catch (e: Exception) {
                _state.value = PokemonListState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
        applySearchFilter(query)
    }

    private fun applySearchFilter(query: String) {
        when (val currentState = _state.value) {
            is PokemonListState.Success -> {
                val filtered = if (query.isBlank()) {
                    allPokemons
                } else {
                    allPokemons.filter {
                        it.name.contains(query, ignoreCase = true)
                    }
                }

                if (filtered.isEmpty()) {
                    _state.value = PokemonListState.Empty
                } else {
                    _state.value = PokemonListState.Success(
                        allPokemons = allPokemons,
                        filteredPokemons = filtered
                    )
                }
            }
            is PokemonListState.Error, PokemonListState.Loading, PokemonListState.Empty -> {
                if (allPokemons.isNotEmpty()) {
                    val filtered = if (query.isBlank()) {
                        allPokemons
                    } else {
                        allPokemons.filter {
                            it.name.contains(query, ignoreCase = true)
                        }
                    }

                    if (filtered.isEmpty()) {
                        _state.value = PokemonListState.Empty
                    } else {
                        _state.value = PokemonListState.Success(
                            allPokemons = allPokemons,
                            filteredPokemons = filtered
                        )
                    }
                }
            }
        }
    }

    fun clearSearch() {
        searchQuery = ""
        if (allPokemons.isNotEmpty()) {
            _state.value = PokemonListState.Success(
                allPokemons = allPokemons,
                filteredPokemons = allPokemons
            )
        } else {
            _state.value = PokemonListState.Empty
        }
    }

    fun toggleFavorite(pokemonId: Int) {
        val currentFavorites = _favorites.value.toMutableSet()
        if (currentFavorites.contains(pokemonId)) {
            currentFavorites.remove(pokemonId)
        } else {
            currentFavorites.add(pokemonId)
        }
        _favorites.value = currentFavorites
    }

    fun onEvent(event: PokemonListEvent) {
        when (event) {
            is PokemonListEvent.Retry -> loadPokemons()
            is PokemonListEvent.ToggleFavorite -> toggleFavorite(event.pokemonId)
            is PokemonListEvent.Search -> updateSearchQuery(event.query)
            is PokemonListEvent.ClearSearch -> clearSearch()
        }
    }
}

sealed class PokemonListEvent {
    object Retry : PokemonListEvent()
    object ClearSearch : PokemonListEvent()
    data class ToggleFavorite(val pokemonId: Int) : PokemonListEvent()
    data class Search(val query: String) : PokemonListEvent()
}