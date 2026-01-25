package com.example.pokedex.data.repository

import com.example.pokedex.data.api.RetrofitInstance
import com.example.pokedex.data.model.PokemonDetail

class PokemonRepository {
    private val api = RetrofitInstance.api

    suspend fun getPokemonList(limit: Int = 20, offset: Int = 0) =
        api.getPokemonList(limit, offset)

    suspend fun getPokemonDetail(id: Int): PokemonDetail =
        api.getPokemonDetail(id)
}