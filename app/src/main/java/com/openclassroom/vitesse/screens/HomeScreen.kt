package com.openclassroom.vitesse.screens

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassroom.vitesse.data.Candidate
import com.openclassroom.vitesse.ui.theme.VitesseTheme
import com.openclassroom.vitesse.data.CandidateData
import java.time.LocalDate
import com.openclassroom.vitesse.data.HomeTab
import java.util.Calendar
import com.openclassroom.vitesse.R


@Composable
fun HomeScreen() {
    var selectedTab by remember { mutableStateOf(HomeTab.ALL) }
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
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
                )
            }
        }

        // Calcul et filtrage avant affichage
        val candidates = when (selectedTab) {
            HomeTab.ALL -> CandidateData.Candidates
            HomeTab.FAVORITE -> CandidateData.FavoriteCandidates
        }.filter {
            it.firstName.contains(searchQuery, ignoreCase = true) ||
                    it.lastName.contains(searchQuery, ignoreCase = true)
        }

        CandidateList(candidates = candidates)
    }

}

@Composable
fun CandidateList(candidates: List<Candidate>) {
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
                CandidateItem(candidate = candidate)
            }
        }
    }
}

@Composable
fun CandidateItem(candidate: Candidate) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val context = LocalContext.current
        val drawableId = remember(candidate.photo) {
            if(candidate.photo.isEmpty()) {
                context.resources.getIdentifier(
                    "no_image_available",
                    "drawable",
                    context.packageName
                )
            } else {
                context.resources.getIdentifier(
                    candidate.photo,
                    "drawable",
                    context.packageName
                )
            }
        }
        Image(
            modifier = Modifier
                .width(54.dp)
                .height(54.dp),
            painter = painterResource(drawableId),
            contentDescription = stringResource(id = R.string.contentDescription_avatar)
        )
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
        HomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun CandidateListPreview() {
    VitesseTheme {
        CandidateList(CandidateData.Candidates)
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
                dateOfBirth = CandidateData.createCalendar(1990, 5, 15),
                email = "alice.dupont@example.com",
                note = "Excellent profil",
                salary = 50000.0,
            )
        )
    }
}
