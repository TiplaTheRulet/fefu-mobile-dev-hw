package com.example.pokedex.presentation.pokemondetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.model.PokemonDetail
import com.example.pokedex.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PokemonDetailState {
    object Loading : PokemonDetailState()
    data class Error(val message: String) : PokemonDetailState()
    data class Success(val pokemon: PokemonDetail) : PokemonDetailState()
}

class PokemonDetailViewModel(pokemonId: Int) : ViewModel() {
    private val repository = PokemonRepository()
    private val _state = MutableStateFlow<PokemonDetailState>(PokemonDetailState.Loading)
    val state: StateFlow<PokemonDetailState> = _state.asStateFlow()

    init {
        loadPokemonDetail(pokemonId)
    }

    private fun loadPokemonDetail(pokemonId: Int) {
        viewModelScope.launch {
            try {
                val pokemon = repository.getPokemonDetail(pokemonId)
                _state.value = PokemonDetailState.Success(pokemon)
            } catch (e: Exception) {
                _state.value = PokemonDetailState.Error(e.message ?: "Unknown error")
            }
        }
    }
}