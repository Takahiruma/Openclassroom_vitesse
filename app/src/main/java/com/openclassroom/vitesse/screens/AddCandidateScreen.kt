package com.openclassroom.vitesse.screens

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassroom.vitesse.R
import com.openclassroom.vitesse.data.Candidate
import com.openclassroom.vitesse.data.CandidateData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCandidateScreen(modifier: Modifier = Modifier,
                       candidate: Candidate? = null,
                       onBackClick: () -> Unit,
                       onSaveClick:(Candidate) -> Unit
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val photo = rememberSaveable { mutableStateOf(candidate?.photo ?: "") }
    val firstName = rememberSaveable { mutableStateOf(candidate?.firstName ?: "") }
    val lastName = rememberSaveable { mutableStateOf(candidate?.lastName ?: "") }
    val email = rememberSaveable { mutableStateOf(candidate?.email ?: "") }
    val note = rememberSaveable { mutableStateOf(candidate?.note ?: "") }
    val salary = rememberSaveable { mutableStateOf(candidate?.salary?.toString() ?: "") }
    val dateOfBirth = rememberSaveable { mutableStateOf(candidate?.dateOfBirth?.timeInMillis ?: 0L) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("Ajouter un candidat")
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (verifyAndCreateCandidate(
                            candidate,
                            photo.value, firstName.value, lastName.value,
                            dateOfBirth.value, email.value, note.value, salary.value,
                            snackbarHostState, scope, context)
                    ) {
                        val savedPhotoPath = photo.value.let { uriString ->
                            val uri = Uri.parse(uriString)
                            saveImageToInternalStorage(context, uri) ?: uriString
                        }
                        photo.value = savedPhotoPath
                        
                        val currentCandidate = Candidate(
                            photo = photo.value,
                            firstName = firstName.value,
                            lastName = lastName.value,
                            dateOfBirth = Calendar.getInstance().apply { timeInMillis = dateOfBirth.value },
                            email = email.value,
                            note = note.value,
                            salary = salary.value.toDoubleOrNull() ?: 0.0
                        )
                        onSaveClick(currentCandidate)
                    }
                }
            ) {
                Text(stringResource(R.string.action_save))
            }
        }
    ) { contentPadding ->
        CreateCandidate(
            modifier = Modifier.padding(contentPadding),
            photo = photo.value,
            onPhotoChanged = { photo.value = it },
            firstName = firstName.value,
            onFirstNameChanged = { firstName.value = it },
            lastName = lastName.value,
            onLastNameChanged = { lastName.value = it },
            email = email.value,
            onEmailChanged = { email.value = it },
            note = note.value,
            onNoteChanged = { note.value = it },
            salary = salary.value,
            onSalaryChanged = { salary.value = it },
            dateOfBirth = dateOfBirth.value,
            onDateOfBirthChanged = { dateOfBirth.value = it }
        )
    }
}

fun saveImageToInternalStorage(context: Context, imageUri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val filename = "candidate_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, filename)
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun verifyAndCreateCandidate(
    candidate: Candidate?,
    photo: String,
    firstName: String,
    lastName: String,
    dateOfBirthMillis: Long,
    email: String,
    note: String,
    salary: String,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    context: Context
): Boolean {
    if (firstName.isBlank()) {
        scope.launch {
            snackbarHostState.showSnackbar(context.getString(R.string.issue_first_name_empty))
        }
        return false
    }

    if (lastName.isBlank()) {
        scope.launch {
            snackbarHostState.showSnackbar(context.getString(R.string.issue_last_name_empty))
        }
        return false
    }

    val dateOfBirth: Calendar = Calendar.getInstance()
    if (dateOfBirthMillis <= 0) {
        scope.launch {
            snackbarHostState.showSnackbar(context.getString(R.string.issue_date_of_birth_invalid))
        }
        return false
    }
    dateOfBirth.timeInMillis = dateOfBirthMillis

    if (email.isBlank()) {
        scope.launch {
            snackbarHostState.showSnackbar(context.getString(R.string.issue_email_empty))
        }
        return false
    }

    // Vérif email simple
    val emailPattern = Regex("^[\\w\\-.]+@[\\w\\-.]+\\.[a-zA-Z]{2,}$")
    if (!email.matches(emailPattern)) {
        scope.launch {
            snackbarHostState.showSnackbar(context.getString(R.string.issue_email_invalid))
        }
        return false
    }

    val candidateSalary: Double
    try {
        candidateSalary = salary.toDouble()
    } catch (e: NumberFormatException) {
        scope.launch {
            snackbarHostState.showSnackbar(context.getString(R.string.issue_invalid_salary))
        }
        return false
    }

    candidate?.let {
        CandidateData.Candidates.remove(it)
    }
    CandidateData.Candidates.add(
        Candidate(
            photo,
            firstName,
            lastName,
            dateOfBirth,
            email,
            note,
            candidateSalary
        )
    )
    return true
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCandidate(
    modifier: Modifier = Modifier,
    photo: String,
    onPhotoChanged: (String) -> Unit,
    firstName: String,
    onFirstNameChanged: (String) -> Unit,
    lastName: String,
    onLastNameChanged: (String) -> Unit,
    email: String,
    onEmailChanged: (String) -> Unit,
    note: String,
    onNoteChanged: (String) -> Unit,
    salary: String,
    onSalaryChanged: (String) -> Unit,
    dateOfBirth: Long,
    onDateOfBirthChanged: (Long) -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            onPhotoChanged(it.toString())
        }
    }

    val photoUri = remember(photo) {
        if (photo.isNotEmpty()) android.net.Uri.parse(photo) else null
    }

    val bitmap = remember(photoUri) {
        photoUri?.let {
            try {
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }


    val calendar = Calendar.getInstance()

    if (dateOfBirth > 0) {
        calendar.timeInMillis = dateOfBirth
    }

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }
                onDateOfBirthChanged(selectedCalendar.timeInMillis)
            },
            year, month, day
        )
    }

    Column(
        modifier = modifier
            .padding(bottom = 88.dp, top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clickable {
                    launcher.launch("image/*")
                }
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Photo du candidat",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.no_image_available),
                    contentDescription = "No image available",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        OutlinedTextField(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            value = firstName,
            onValueChange = onFirstNameChanged,
            label = { Text("Prénom") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            value = lastName,
            onValueChange = onLastNameChanged,
            label = { Text("Nom") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            value = email,
            onValueChange = onEmailChanged,
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            value = if (dateOfBirth > 0) SimpleDateFormat("yyyy-MM-dd").format(Date(dateOfBirth)) else "",
            onValueChange = { /* read only */ },
            label = { Text("Date de naissance") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    datePickerDialog.show()
                }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Choisir date")
                }
            }
        )


        OutlinedTextField(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            value = salary,
            onValueChange = onSalaryChanged,
            label = { Text("Salaire") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )


        OutlinedTextField(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            value = note,
            onValueChange = onNoteChanged,
            label = { Text("Note") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = false,
            maxLines = 3
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Écran ajout candidat"
)
@Composable
fun CreateCandidateScreenPreview() {
    val dateOfBirthMillis = Calendar.getInstance().apply {
        set(1990, Calendar.JANUARY, 1)
    }.timeInMillis

    CreateCandidate(
        modifier = Modifier,
        photo = "https://exemple.com/photo.jpg",
        onPhotoChanged = {},
        firstName = "Jean",
        onFirstNameChanged = {},
        lastName = "Dupont",
        onLastNameChanged = {},
        email = "jean.dupont@email.com",
        onEmailChanged = {},
        note = "Très motivé !",
        onNoteChanged = {},
        salary = "2500.0",
        onSalaryChanged = {},
        dateOfBirth = dateOfBirthMillis,
        onDateOfBirthChanged = {}
    )
}