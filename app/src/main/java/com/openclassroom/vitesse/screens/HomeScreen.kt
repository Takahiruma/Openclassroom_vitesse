package com.openclassroom.vitesse.screens

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
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
    var isLoading by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize().statusBarsPadding().background(Color.White)) {
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
    }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        isLoading = false
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

        CandidateImage(photoUriString = candidate.photo)

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

@Composable
fun CandidateImage(photoUriString: String) {
    val context = LocalContext.current
    val bitmap = remember(photoUriString) {
        try {
            if (photoUriString.isNotEmpty()) {
                val uri = Uri.parse(photoUriString)
                context.contentResolver.openInputStream(uri).use { stream ->
                    BitmapFactory.decodeStream(stream)
                }
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "candidate picture",
            modifier = Modifier.size(54.dp),
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(R.drawable.no_image_available),
            contentDescription = "No image",
            modifier = Modifier.size(54.dp)
        )
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
                phoneNumber = "0612345678",
                dateOfBirth = CandidateData.createCalendar(1990, 5, 15),
                email = "alice.dupont@example.com",
                note = "Excellent profil",
                salary = 50000.0,
            )
        )
    }
}
