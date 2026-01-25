package com.example.pokedex.presentation.navigation

sealed class Screens(val route: String) {
    object PokemonList : Screens("pokemon_list")
    object PokemonDetail : Screens("pokemon_detail/{pokemonId}") {
        fun createRoute(pokemonId: Int) = "pokemon_detail/$pokemonId"
    }
}