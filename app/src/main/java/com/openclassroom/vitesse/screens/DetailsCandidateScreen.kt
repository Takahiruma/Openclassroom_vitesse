package com.openclassroom.vitesse.screens

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.openclassroom.vitesse.R
import com.openclassroom.vitesse.data.Candidate
import com.openclassroom.vitesse.ui.theme.VitesseTheme
import com.openclassroom.vitesse.utils.CandidateImage
import com.openclassroom.vitesse.viewModel.ExchangeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsCandidateScreen(
    modifier: Modifier = Modifier,
    candidate: Candidate?,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onFavoriteToggle: (Candidate) -> Unit,
    onDeleteClick: () -> Unit,
    exchangeViewModel: ExchangeViewModel,
) {
    rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }

    val conversionRate by exchangeViewModel.conversionRate.collectAsState()

    LaunchedEffect(candidate) {
        candidate?.let {
            exchangeViewModel.loadConversionRate("eur", "gbp")
        }
    }

    Scaffold(
        modifier = modifier.background(Color.White),
        topBar = {
            TopAppBar(
                title = {
                    Text(candidate?.firstName + " " + candidate?.lastName)
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }, modifier = Modifier.semantics { role = Role.Button }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                modifier = Modifier
                    .height(64.dp)
                    .background(MaterialTheme.colorScheme.surface),
                actions = {
                    candidate?.let {
                        IconButton(onClick = {
                            onFavoriteToggle(it)
                            },
                            modifier = Modifier.semantics { role = Role.Button }
                        ) {
                            Icon(
                                imageVector = if (it.isFavorite) Icons.Filled.Star else ImageVector.vectorResource(id = R.drawable.outline_star),
                                contentDescription = stringResource(id = R.string.favorite)
                            )
                        }
                        IconButton(onClick = onEditClick, modifier = Modifier.semantics { role = Role.Button }) {
                            Icon(Icons.Default.Edit, contentDescription = "Modifier")
                        }
                        IconButton(onClick = {
                            showDeleteDialog = true
                            },
                            modifier = Modifier.semantics { role = Role.Button }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.deletion))
                        }

                        if (showDeleteDialog) {
                            AlertDialog(
                                onDismissRequest = { showDeleteDialog = false },
                                title = { Text(text = stringResource(R.string.deletion)) },
                                text = { Text(text = stringResource(R.string.confirm_message)) },
                                confirmButton = {
                                    TextButton(onClick = {
                                        showDeleteDialog = false
                                        onDeleteClick()
                                        },
                                        modifier = Modifier.semantics { role = Role.Button }
                                    ) {
                                        Text(text = stringResource(R.string.confirm))
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = {
                                        showDeleteDialog = false
                                        },
                                        modifier = Modifier.semantics { role = Role.Button }
                                    ) {
                                        Text(text = stringResource(R.string.cancel))
                                    }
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
            )
        },
        content = { paddingValues ->
            candidate?.let {
                DetailCandidate(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    photo = it.photo,
                    phoneNumber = it.phoneNumber,
                    email = it.email,
                    note = it.note,
                    salary = it.salary.toString(),
                    conversionRate = conversionRate,
                    dateOfBirth = it.dateOfBirth.timeInMillis
                )
            }
        }
    )
}


@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailCandidate(
    modifier: Modifier = Modifier,
    photo: String,
    phoneNumber: String,
    email: String,
    note: String,
    salary: String,
    conversionRate: Double,
    dateOfBirth: Long,
){
    val scrollState = rememberScrollState()
    LocalContext.current

    val dateFormatter = remember {
        SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, Locale.getDefault())
    }

    val birthCalendar = Calendar.getInstance().apply { timeInMillis = dateOfBirth }
    val today = Calendar.getInstance()
    val age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR) -
            if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) 1 else 0

    val salaryEuro = salary.toDoubleOrNull() ?: 0.0
    val salaryPound = salaryEuro * conversionRate
    val salaryPoundFormatted = String.format("%.2f", salaryPound)

    Column(
        modifier = modifier
            .padding(bottom = 88.dp, top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ){
        Box(
            modifier = Modifier
                .aspectRatio(3f / 2f)
        ) {
            CandidateImage(photoUriString = "file://$photo", modifier = Modifier.fillMaxSize())
        }

        ContactIconsRow(phoneNumber, email)

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.about),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "${dateFormatter.format(Date(dateOfBirth))} ($age)",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(id = R.string.birthday),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.salary),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "$salaryEuro €",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "$salaryPoundFormatted £",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.notes),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = note,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun ContactIconsRow(phoneNumber: String, emailAddress: String) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val icons = listOf(
            Triple(
                Icons.Default.Call,
                R.string.call
            ) {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = "tel:$phoneNumber".toUri()
                }
                context.startActivity(intent)
            },
            Triple(
                Icons.Default.Sms,
                R.string.sms
            ) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "smsto:$phoneNumber".toUri()
                }
                context.startActivity(intent)
            },
            Triple(
                Icons.Default.Email,
                R.string.mail_address
            ) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:$emailAddress".toUri()
                }
                context.startActivity(intent)
            }
        )

        icons.forEachIndexed { index, (icon, textRes, onClick) ->
            Column(
                modifier = Modifier.padding(end = if (index < icons.size - 1) 10.dp else 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = onClick,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            shape = CircleShape
                        )
                        .semantics { role = Role.Button }
                ) {
                    Icon(
                        icon,
                        contentDescription = stringResource(id = textRes),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = stringResource(id = textRes),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsCandidateFavoriteScreenPreview() {
    val exchangeViewModel: ExchangeViewModel = viewModel()

    val exampleCandidate = Candidate(
        id = 1,
        photo = "",
        firstName = "John",
        lastName = "Doe",
        phoneNumber = "0612345678",
        dateOfBirth = Calendar.getInstance().apply {
            set(1985, 5, 15)
        },
        email = "john.doe@example.com",
        note = "Experienced Kotlin developer with focus on Android and Compose.",
        salary = 55000.0,
        isFavorite = true
    )

    VitesseTheme {
        DetailsCandidateScreen(
            candidate = exampleCandidate,
            onBackClick = {},
            onEditClick = {},
            onFavoriteToggle = {},
            onDeleteClick = {},
            exchangeViewModel = exchangeViewModel,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsCandidateScreenPreview() {
    val exchangeViewModel: ExchangeViewModel = viewModel()

    val exampleCandidate = Candidate(
        id = 1,
        photo = "",
        firstName = "John",
        lastName = "Doe",
        phoneNumber = "0612345678",
        dateOfBirth = Calendar.getInstance().apply {
            set(1985, Calendar.JUNE, 15)
        },
        email = "john.doe@example.com",
        note = "Experienced Kotlin developer with focus on Android and Compose.",
        salary = 55000.0,
        isFavorite = false
    )
    VitesseTheme {
        DetailsCandidateScreen(
            candidate = exampleCandidate,
            onBackClick = {},
            onEditClick = {},
            onFavoriteToggle = {},
            onDeleteClick = {},
            exchangeViewModel = exchangeViewModel,
        )
    }
}
