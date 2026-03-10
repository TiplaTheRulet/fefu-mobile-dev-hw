package com.example.swfilmfinder.domain.repository

import com.example.swfilmfinder.domain.model.Film
import com.example.swfilmfinder.domain.model.FilmDetail

interface FilmRepository {
    suspend fun getFilms(): List<Film>
    suspend fun getFilmDetails(id: Int): FilmDetail
}