package com.example.swfilmfinder.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FilmListResponseDto(
    @SerializedName("results") val results: List<FilmDto>
)
