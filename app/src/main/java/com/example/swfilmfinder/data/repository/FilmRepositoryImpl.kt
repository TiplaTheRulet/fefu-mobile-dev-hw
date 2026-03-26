package com.example.swfilmfinder.data.repository

import com.example.swfilmfinder.data.remote.SwapiService
import com.example.swfilmfinder.domain.mapper.toDetailDomain
import com.example.swfilmfinder.domain.mapper.toDomain
import com.example.swfilmfinder.domain.model.Film
import com.example.swfilmfinder.domain.model.FilmDetail
import com.example.swfilmfinder.domain.repository.FilmRepository
import javax.inject.Inject

class FilmRepositoryImpl @Inject constructor(
    private val api: SwapiService
) : FilmRepository {

    override suspend fun getFilms(): List<Film> {
        return api.getFilms().results.map { it.toDomain() }
    }

    override suspend fun getFilmDetails(id: Int): FilmDetail {
        return api.getFilmDetails(id).toDetailDomain()
    }
}
