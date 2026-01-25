package com.example.pokedex.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokedex.presentation.pokemondetail.PokemonDetailScreen
import com.example.pokedex.presentation.pokemonlist.PokemonListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.PokemonList.route
    ) {
        composable(Screens.PokemonList.route) {
            PokemonListScreen(navController = navController)
        }
        composable(Screens.PokemonDetail.route) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonId")?.toIntOrNull() ?: 0
            PokemonDetailScreen(pokemonId = pokemonId, navController = navController)
        }
    }
}