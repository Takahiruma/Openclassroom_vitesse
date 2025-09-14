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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.openclassroom.vitesse.data.Candidate
import com.openclassroom.vitesse.repository.CandidateRepository
import com.openclassroom.vitesse.screens.AddCandidateScreen
import com.openclassroom.vitesse.ui.theme.VitesseTheme
import com.openclassroom.vitesse.screens.HomeScreen
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
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainerHigh),
                    floatingActionButton = {
                        if (currentDestination?.route != "addCandidateScreen") {
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
                        modifier = Modifier.padding(paddingValues).background(Color.White)
                    ) {
                        composable("home") {
                            HomeScreen(
                            )
                        }
                        composable("addCandidateScreen") {
                            AddCandidateScreen(
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
                    }
                }
            }
        }
    }
}
