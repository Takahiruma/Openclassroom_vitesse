package com.openclassroom.vitesse

import CandidateViewModel
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.openclassroom.vitesse.data.Candidate
import com.openclassroom.vitesse.data.CandidateData
import com.openclassroom.vitesse.repository.CandidateRepository
import com.openclassroom.vitesse.screens.AddCandidateScreen
import com.openclassroom.vitesse.screens.DetailsCandidateScreen
import com.openclassroom.vitesse.screens.HomeScreen
import com.openclassroom.vitesse.ui.theme.VitesseTheme
import com.openclassroom.vitesse.viewModel.CandidateViewModelFactory

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            VitesseTheme {
                val navController = rememberNavController()
                val viewModel: CandidateViewModel = viewModel(
                    factory = CandidateViewModelFactory(CandidateRepository())
                )
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStackEntry?.destination

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                    floatingActionButton = {
                        Log.d("FloatingActionButton", "page ${currentDestination?.route} ")
                        if (currentDestination?.route == "home") {
                            FloatingActionButton(
                                onClick = { navController.navigate("addCandidateScreen") }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Ajouter un candidat")
                            }
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier
                            .padding(paddingValues)
                            .background(Color.White)
                    ) {
                        composable("home") {
                            HomeScreen(
                                viewModel,
                                onCandidateClick = { candidateId ->
                                    navController.navigate("details/$candidateId")
                                }
                            )
                        }
                        composable("addCandidateScreen") {
                            AddCandidateScreen(
                                onBackClick = { navController.popBackStack() },
                                onSaveClick = { newCandidate ->
                                    viewModel.addOrUpdateCandidate(newCandidate)
                                    navController.popBackStack()
                                },
                                modifier = Modifier,
                            )
                        }
                        composable("addCandidateScreen/{candidateId}",
                            arguments = listOf(navArgument("candidateId") { type = NavType.StringType; nullable = true })
                            ) { backStackEntry ->
                            val candidateId = backStackEntry.arguments?.getString("candidateId")

                            val candidates by viewModel.candidates.collectAsState()

                            AddCandidateScreen(
                                candidate = candidates.firstOrNull{it.id.toString() == candidateId},
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onSaveClick = { candidate ->
                                    try {
                                        viewModel.addCandidate(candidate)
                                        navController.popBackStack()
                                    } catch (e: Exception) {
                                        Log.e("SaveError", "Erreur lors de la sauvegarde: ", e)
                                    }
                                },
                                modifier = Modifier,
                            )
                        }
                        composable(
                            "details/{candidateId}",
                            arguments = listOf(navArgument("candidateId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val candidateId = backStackEntry.arguments?.getInt("candidateId")

                            val candidates by viewModel.candidates.collectAsState()

                            val candidate = candidates.find { it.id == candidateId }

                            DetailsCandidateScreen(
                                candidate = candidate,
                                onBackClick = { navController.popBackStack() },
                                onEditClick = { navController.navigate("addCandidateScreen/${candidate?.id}") },
                                onFavoriteToggle = { candidate ->
                                    viewModel.toggleFavorite(candidate)
                                },
                                onDeleteClick = {  }
                            )
                        }
                    }
                }
            }
        }
    }
}
