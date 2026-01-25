package com.example.splitmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.splitmate.screens.HomeScreen
import com.example.splitmate.screens.InputScreen
import com.example.splitmate.screens.ResultScreen
import com.example.splitmate.theme.SplitMateTheme
import com.example.splitmate.viewmodel.CalculationViewModel

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplitMateTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: CalculationViewModel = viewModel()

                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(
                                onStartClick = {
                                    navController.navigate("input")
                                }
                            )
                        }

                        composable("input") {
                            InputScreen(
                                viewModel = viewModel,
                                onCalculateClick = { calculationId ->
                                    navController.navigate("result/$calculationId")
                                },
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = "result/{calcId}",
                            arguments = listOf(
                                navArgument("calcId") {
                                    type = NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            val calcId = backStackEntry.arguments?.getString("calcId") ?: ""
                            ResultScreen(
                                viewModel = viewModel,
                                calculationId = calcId,
                                onEditClick = { navController.popBackStack() },
                                onNewCalculationClick = {
                                    viewModel.resetCalculation()
                                    navController.popBackStack("home", inclusive = false)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
