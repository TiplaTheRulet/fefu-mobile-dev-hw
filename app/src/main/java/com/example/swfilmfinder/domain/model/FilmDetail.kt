package com.example.swfilmfinder.domain.model

data class FilmDetail(
    val id: Int,
    val title: String,
    val openingCrawl: String,
    val director: String,
    val producer: String,
    val releaseDate: String,

    val characterCount: Int,
    val planetsCount: Int,
    val starshipsCount: Int,
    val vehiclesCount: Int,
    val speciesCount: Int,
    val yearsSinceRelease: Int
)
