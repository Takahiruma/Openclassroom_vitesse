package com.openclassroom.vitesse

import CandidateViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(id = R.string.app_name)) }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { navController.navigate("addCandidateScreen") }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Ajouter un candidat")
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(paddingValues)
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
                                    viewModel.addCandidate(candidate)
                                    navController.popBackStack()
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
