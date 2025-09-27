package com.openclassroom.vitesse.screens

import CandidateViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.openclassroom.vitesse.data.Candidate
import com.openclassroom.vitesse.ui.theme.VitesseTheme
import com.openclassroom.vitesse.data.CandidateData
import com.openclassroom.vitesse.data.HomeTab
import com.openclassroom.vitesse.R
import com.openclassroom.vitesse.utils.CandidateImage
import com.openclassroom.vitesse.utils.createCalendar


@Composable
fun HomeScreen(viewModel: CandidateViewModel = viewModel (),
               onCandidateClick: (String) -> Unit) {
    var selectedTab by remember { mutableStateOf(HomeTab.ALL) }
    var searchQuery by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()

    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .background(Color.White)) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Column {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(stringResource(R.string.Search)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(25.dp),
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    )
                )

                TabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    HomeTab.entries.forEach { tab ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            text = { Text(stringResource(tab.titleResId)) },
                            modifier = Modifier.background(Color.White),
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                val candidates by viewModel.candidates.collectAsState()

                val filteredCandidates  = when (selectedTab) {
                    HomeTab.ALL -> candidates
                    HomeTab.FAVORITE -> candidates.filter { it.isFavorite }
                }.filter {
                    it.firstName.contains(searchQuery, ignoreCase = true) ||
                            it.lastName.contains(searchQuery, ignoreCase = true)
                }

                CandidateList(candidates = filteredCandidates, onCandidateClick = onCandidateClick)
            }
        }
    }
}

@Composable
fun CandidateList(candidates: List<Candidate>,
                  onCandidateClick: (String) -> Unit
) {
    if (candidates.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.Empty_candidate_list))
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(candidates) { candidate ->
                CandidateItem(candidate = candidate, onClick = { onCandidateClick(candidate.id.toString()) })
            }
        }
    }
}

@Composable
fun CandidateItem(candidate: Candidate, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        CandidateImage(photoUriString = "file://${candidate.photo}" , modifier = Modifier.size(54.dp))

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${candidate.firstName} ${candidate.lastName}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${candidate.note}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    VitesseTheme {
        HomeScreen(onCandidateClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun CandidateListPreview() {
    VitesseTheme {
        CandidateList(CandidateData.Candidates,
            onCandidateClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun CandidateItemPreview() {
    VitesseTheme {
        CandidateItem(
            Candidate(
                photo = "",
                firstName = "Alice",
                lastName = "Dupont",
                phoneNumber = "0612345678",
                dateOfBirth = createCalendar(1990, 5, 15),
                email = "alice.dupont@example.com",
                note = "Excellent profil",
                salary = 50000.0,
            ),
            onClick = {}
        )
    }
}
