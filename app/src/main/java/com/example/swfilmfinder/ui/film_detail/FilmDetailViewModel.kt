package com.example.swfilmfinder.ui.film_detail

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swfilmfinder.domain.model.FilmDetail
import com.example.swfilmfinder.domain.repository.FilmRepository
import com.example.swfilmfinder.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FilmDetailViewModel @Inject constructor(
    private val repository: FilmRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val filmId: Int = checkNotNull(savedStateHandle["filmId"])

    private val _uiState = MutableStateFlow<UiState<FilmDetail>>(UiState.Loading)
    val uiState: StateFlow<UiState<FilmDetail>> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    fun loadDetail() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val detail = repository.getFilmDetails(filmId)
                _uiState.value = UiState.Success(detail)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Ошибка загрузки деталей: ${e.localizedMessage}")
            }
        }
    }
}
