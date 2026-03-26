package com.example.swfilmfinder.ui.film_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swfilmfinder.domain.model.Film
import com.example.swfilmfinder.domain.repository.FilmRepository
import com.example.swfilmfinder.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmListViewModel @Inject constructor(
    private val repository: FilmRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Film>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Film>>> = _uiState.asStateFlow()

    init {
        loadFilms()
    }

    fun loadFilms() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val films = repository.getFilms()
                _uiState.value = UiState.Success(films)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Ошибка загрузки данных: ${e.localizedMessage}")
            }
        }
    }
}
