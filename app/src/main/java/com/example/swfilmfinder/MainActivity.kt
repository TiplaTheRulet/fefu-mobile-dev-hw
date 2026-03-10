package com.example.swfilmfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.swfilmfinder.ui.film_detail.FilmDetailScreen
import com.example.swfilmfinder.ui.film_list.FilmListScreen
import com.example.swfilmfinder.ui.theme.SWFilmFinderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SWFilmFinderTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "list") {
                    composable("list") {
                        FilmListScreen(
                            onFilmClick = { filmId ->
                                navController.navigate("detail/$filmId")
                            }
                        )
                    }
                    composable(
                        route = "detail/{filmId}",
                        arguments = listOf(navArgument("filmId") { type = NavType.IntType })
                    ) {
                        FilmDetailScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
