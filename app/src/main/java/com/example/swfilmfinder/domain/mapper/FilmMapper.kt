package com.example.swfilmfinder.domain.mapper

import com.example.swfilmfinder.data.remote.dto.FilmDto
import com.example.swfilmfinder.domain.model.Film
import com.example.swfilmfinder.domain.model.FilmDetail
import java.time.LocalDate

fun extractIdFromUrl(url: String): Int {
    return url.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: 0
}

fun FilmDto.toDomain(): Film {
    return Film(
        id = extractIdFromUrl(this.url),
        title = this.title,
        releaseDate = this.releaseDate
    )
}

fun FilmDto.toDetailDomain(): FilmDetail {
    val year = this.releaseDate.substringBefore("-").toIntOrNull() ?: 0
    val currentYear = LocalDate.now().year
    val age = if (year > 0) currentYear - year else 0

    return FilmDetail(
        id = extractIdFromUrl(this.url),
        title = this.title,
        director = this.director,
        producer = this.producer,
        openingCrawl = this.openingCrawl ?: "",
        releaseDate = this.releaseDate,
        characterCount = this.characters?.size ?: 0,
        planetsCount = this.planets?.size ?: 0,
        starshipsCount = this.starships?.size ?: 0,
        vehiclesCount = this.vehicles?.size ?: 0,
        speciesCount = this.species?.size ?: 0,
        yearsSinceRelease = age
    )
}
