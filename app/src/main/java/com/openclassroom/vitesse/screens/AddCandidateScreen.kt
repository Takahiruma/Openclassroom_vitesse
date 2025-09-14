package com.openclassroom.vitesse.screens

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassroom.vitesse.R
import com.openclassroom.vitesse.data.Candidate
import com.openclassroom.vitesse.data.CandidateData
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import androidx.core.net.toUri


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
    val phoneNumber = rememberSaveable { mutableStateOf(candidate?.phoneNumber ?: "") }
    val email = rememberSaveable { mutableStateOf(candidate?.email ?: "") }
    val note = rememberSaveable { mutableStateOf(candidate?.note ?: "") }
    val salary = rememberSaveable { mutableStateOf(candidate?.salary?.toString() ?: "") }
    val dateOfBirth = rememberSaveable { mutableStateOf(candidate?.dateOfBirth?.timeInMillis ?: 0L) }

    val firstNameError = remember { mutableStateOf(false) }
    val lastNameError = remember { mutableStateOf(false) }
    val phoneNumberError = remember { mutableStateOf(false) }
    val emailError = remember { mutableStateOf(false) }
    val salaryError = remember { mutableStateOf(false) }
    val dateOfBirthError = remember { mutableStateOf(false) }

    val firstNameErrorMessage = remember { mutableStateOf("") }
    val lastNameErrorMessage = remember { mutableStateOf("") }
    val phoneNumberErrorMessage = remember { mutableStateOf("") }
    val emailErrorMessage = remember { mutableStateOf("") }
    val salaryErrorMessage = remember { mutableStateOf("") }
    val dateOfBirthErrorMessage = remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.background(Color.White),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.add_candidate))
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                modifier = Modifier
                    .height(64.dp)
                    .background(MaterialTheme.colorScheme.surface),
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (verifyAndCreateCandidate(
                            candidate,
                            photo.value, firstName.value, lastName.value, phoneNumber.value,
                            dateOfBirth.value, email.value, note.value, salary.value,
                            firstNameError, lastNameError, phoneNumberError, emailError,
                            salaryError, dateOfBirthError,
                            firstNameErrorMessage, lastNameErrorMessage, phoneNumberErrorMessage,
                            emailErrorMessage, salaryErrorMessage, dateOfBirthErrorMessage,
                            context)
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
                            phoneNumber = phoneNumber.value,
                            note = note.value,
                            salary = salary.value.toDoubleOrNull() ?: 0.0
                        )
                        onSaveClick(currentCandidate)
                    }
                },

                containerColor = MaterialTheme.colorScheme.primary,
                contentColor =  MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                content = {
                    Text(
                        text = stringResource(R.string.action_save),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )

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
            phoneNumber = phoneNumber.value,
            onPhoneNumberChanged = { phoneNumber.value = it },
            email = email.value,
            onEmailChanged = { email.value = it },
            note = note.value,
            onNoteChanged = { note.value = it },
            salary = salary.value,
            onSalaryChanged = { salary.value = it },
            dateOfBirth = dateOfBirth.value,
            onDateOfBirthChanged = { dateOfBirth.value = it },
            firstNameError = firstNameError,
            lastNameError = lastNameError,
            phoneNumberError = phoneNumberError,
            emailError = emailError,
            salaryError = salaryError,
            dateOfBirthError = dateOfBirthError,
            firstNameErrorMessage = firstNameErrorMessage,
            lastNameErrorMessage = lastNameErrorMessage,
            phoneNumberErrorMessage = phoneNumberErrorMessage,
            emailErrorMessage = emailErrorMessage,
            salaryErrorMessage = salaryErrorMessage,
            dateOfBirthErrorMessage = dateOfBirthErrorMessage
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
    phoneNumber: String,
    dateOfBirthMillis: Long,
    email: String,
    note: String,
    salary: String,
    firstNameError: MutableState<Boolean>,
    lastNameError: MutableState<Boolean>,
    phoneNumberError: MutableState<Boolean>,
    emailError: MutableState<Boolean>,
    salaryError: MutableState<Boolean>,
    dateOfBirthError: MutableState<Boolean>,
    firstNameErrorMessage: MutableState<String>,
    lastNameErrorMessage: MutableState<String>,
    phoneNumberErrorMessage: MutableState<String>,
    emailErrorMessage: MutableState<String>,
    salaryErrorMessage: MutableState<String>,
    dateOfBirthErrorMessage: MutableState<String>,
    context: Context
): Boolean {
    firstNameError.value = false
    lastNameError.value = false
    phoneNumberError.value = false
    emailError.value = false
    salaryError.value = false
    dateOfBirthError.value = false

    var isValid = true

    if (firstName.isBlank()) {
        firstNameError.value = true
        firstNameErrorMessage.value = context.getString(R.string.mandatory_field)
        isValid = false
    }

    if (lastName.isBlank()) {
        lastNameError.value = true
        lastNameErrorMessage.value = context.getString(R.string.mandatory_field)
        isValid = false
    }

    if (phoneNumber.isBlank()) {
        phoneNumberError.value = true
        phoneNumberErrorMessage.value = context.getString(R.string.mandatory_field)
        isValid = false
    } else {
        val phoneRegex = Regex("^(0|\\+33|0033)[1-9](\\d{2}){4}$")
        val normalizedPhone = phoneNumber.replace("[\\s.-]".toRegex(), "")
        if (!phoneRegex.matches(normalizedPhone)) {
            phoneNumberError.value = true
            phoneNumberErrorMessage.value = context.getString(R.string.invalid_format)
            isValid = false
        }
    }

    val dateOfBirth: Calendar = Calendar.getInstance()
    if (dateOfBirthMillis <= 0) {
        dateOfBirthError.value = true
        dateOfBirthErrorMessage.value = context.getString(R.string.mandatory_field)
        isValid = false
    } else {
        dateOfBirth.timeInMillis = dateOfBirthMillis
        val currentDate = Calendar.getInstance()
        if (dateOfBirth.timeInMillis > currentDate.timeInMillis) {
            dateOfBirthError.value = true
            dateOfBirthErrorMessage.value = context.getString(R.string.issue_future_date)
            isValid = false
        }
    }

    if (email.isBlank()) {
        emailError.value = true
        emailErrorMessage.value = context.getString(R.string.mandatory_field)
        isValid = false
    } else {
        val emailPattern = Regex("^[\\w\\-.]+@[\\w\\-.]+\\.[a-zA-Z]{2,}$")
        if (!email.matches(emailPattern)) {
            emailError.value = true
            emailErrorMessage.value = context.getString(R.string.invalid_format)
            isValid = false
        }
    }

    var candidateSalary: Double
    try {
        candidateSalary = salary.toDouble()
        if (candidateSalary < 0) {
            salaryError.value = true
            salaryErrorMessage.value = context.getString(R.string.mandatory_field)
            isValid = false
        }
    } catch (e: NumberFormatException) {
        salaryError.value = true
        salaryErrorMessage.value = context.getString(R.string.mandatory_field)
        isValid = false
        candidateSalary = 0.0
    }

    if (!isValid) {
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
            phoneNumber,
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
    phoneNumber: String,
    onPhoneNumberChanged: (String) -> Unit,
    email: String,
    onEmailChanged: (String) -> Unit,
    note: String,
    onNoteChanged: (String) -> Unit,
    salary: String,
    onSalaryChanged: (String) -> Unit,
    dateOfBirth: Long,
    onDateOfBirthChanged: (Long) -> Unit,
    firstNameError: MutableState<Boolean> = remember { mutableStateOf(false) },
    lastNameError: MutableState<Boolean> = remember { mutableStateOf(false) },
    phoneNumberError: MutableState<Boolean> = remember { mutableStateOf(false) },
    emailError: MutableState<Boolean> = remember { mutableStateOf(false) },
    salaryError: MutableState<Boolean> = remember { mutableStateOf(false) },
    dateOfBirthError: MutableState<Boolean> = remember { mutableStateOf(false) },
    firstNameErrorMessage: MutableState<String> = remember { mutableStateOf("") },
    lastNameErrorMessage: MutableState<String> = remember { mutableStateOf("") },
    phoneNumberErrorMessage: MutableState<String> = remember { mutableStateOf("") },
    emailErrorMessage: MutableState<String> = remember { mutableStateOf("") },
    salaryErrorMessage: MutableState<String> = remember { mutableStateOf("") },
    dateOfBirthErrorMessage: MutableState<String> = remember { mutableStateOf("") }
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            onPhotoChanged(it.toString())
        }
    }

    val photoUri = remember(photo) {
        if (photo.isNotEmpty()) photo.toUri() else null
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

    val currentDate = Calendar.getInstance()

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }

                if (selectedCalendar.timeInMillis <= currentDate.timeInMillis) {
                    onDateOfBirthChanged(selectedCalendar.timeInMillis)
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.issue_future_date),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            year, month, day
        ).apply {
            datePicker.maxDate = currentDate.timeInMillis
        }
    }

    @Composable
    fun LabelledTextField(
        labelId: Int,
        value: String,
        onValueChange: (String) -> Unit,
        keyboardType: KeyboardType = KeyboardType.Text,
        singleLine: Boolean = true,
        maxLines: Int = 1,
        readOnly: Boolean = false,
        trailingIcon: @Composable (() -> Unit)? = null,
        isError: Boolean = false,
        errorMessage: String = ""
    ) {
        val focusRequester = remember { FocusRequester() }
        Column(modifier = Modifier.padding(top = 16.dp, start = 32.dp)) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(stringResource(id = labelId)) },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                singleLine = singleLine,
                maxLines = maxLines,
                readOnly = readOnly,
                trailingIcon = trailingIcon,
                modifier = Modifier.fillMaxWidth()
                    .focusRequester(focusRequester)
                    .clickable { focusRequester.requestFocus() },
                isError = isError,
                colors = OutlinedTextFieldDefaults.colors(
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorTrailingIconColor = MaterialTheme.colorScheme.error,
                    errorCursorColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error
                )
            )
            if (isError && errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }
        }
    }

    @Composable
    fun LabelledIconTextField(
        labelId: Int,
        icon: ImageVector,
        value: String,
        onValueChange: (String) -> Unit,
        keyboardType: KeyboardType = KeyboardType.Text,
        singleLine: Boolean = true,
        maxLines: Int = 1,
        readOnly: Boolean = false,
        trailingIcon: @Composable (() -> Unit)? = null,
        isError: Boolean = false,
        errorMessage: String = ""
    ) {
        val focusRequester = remember { FocusRequester() }
        Row(
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Icon of " + stringResource(id = labelId),
                modifier = Modifier.padding(end = 8.dp)
                    .then(
                        if (labelId == R.string.notes) Modifier else Modifier.align(Alignment.CenterVertically)
                    ),
            )
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    label = {
                            Text(stringResource(id = labelId))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    singleLine = singleLine,
                    maxLines = maxLines,
                    readOnly = readOnly,
                    trailingIcon = trailingIcon,
                    modifier = Modifier.fillMaxWidth()
                        .then(
                        if (labelId == R.string.notes) Modifier.height(175.dp) else Modifier
                        )
                        .focusRequester(focusRequester)
                        .clickable { focusRequester.requestFocus() },
                    isError = isError,
                    colors = OutlinedTextFieldDefaults.colors(
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        errorTrailingIconColor = MaterialTheme.colorScheme.error,
                        errorCursorColor = MaterialTheme.colorScheme.error,
                        errorLabelColor = MaterialTheme.colorScheme.error
                    )
                )
                if (isError && errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }
            }
        }
    }

    Column(
        modifier = modifier
            .padding(bottom = 88.dp, top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(3f / 2f)
                .clickable {
                    launcher.launch("image/*")
                }
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = stringResource(id = R.string.candidate_picture),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.no_image_available),
                    contentDescription = stringResource(id = R.string.no_image_available),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        LabelledIconTextField(
            labelId = R.string.first_name,
            icon = Icons.Default.Person,
            value = firstName,
            onValueChange = onFirstNameChanged,
            isError = firstNameError.value,
            errorMessage = firstNameErrorMessage.value
        )

        LabelledTextField(
            labelId = R.string.last_name,
            value = lastName,
            onValueChange = onLastNameChanged,
            isError = lastNameError.value,
            errorMessage = lastNameErrorMessage.value
        )

        LabelledIconTextField(
            labelId = R.string.phone_number,
            icon = Icons.Default.Phone,
            value = phoneNumber,
            onValueChange = onPhoneNumberChanged,
            keyboardType = KeyboardType.Phone,
            isError = phoneNumberError.value,
            errorMessage = phoneNumberErrorMessage.value
        )

        LabelledIconTextField(
            labelId = R.string.mail_address,
            icon = Icons.Default.Email,
            value = email,
            onValueChange = onEmailChanged,
            keyboardType = KeyboardType.Email,
            isError = emailError.value,
            errorMessage = emailErrorMessage.value
        )

        Row(
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.cake),
                contentDescription = "cake",
                modifier = Modifier.padding(end = 8.dp),
            )
            Column(
                modifier = Modifier
                    .size(width = 322.dp, height = 218.dp)
                    .padding(top = 16.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh, RoundedCornerShape(28.dp))
            ) {
                Text(
                    text = stringResource(id = R.string.date_of_birth),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top=16.dp).align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(36.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePickerDialog.show() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.enter_date_of_birth),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.calendar),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                val focusRequester = remember { FocusRequester() }
                OutlinedTextField(
                    value = if (dateOfBirth > 0) SimpleDateFormat("dd/MM/yyyy").format(Date(dateOfBirth)) else "",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                        .focusRequester(focusRequester)
                        .clickable { focusRequester.requestFocus() },
                    isError = dateOfBirthError.value,
                    placeholder = {Text("jj/mm/aaaa")},
                    colors = OutlinedTextFieldDefaults.colors(
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        errorTrailingIconColor = MaterialTheme.colorScheme.error,
                        errorCursorColor = MaterialTheme.colorScheme.error,
                        errorLabelColor = MaterialTheme.colorScheme.error
                    )
                )
                if (dateOfBirthError.value && dateOfBirthErrorMessage.value.isNotEmpty()) {
                    Text(
                        text = dateOfBirthErrorMessage.value,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }
            }
        }

        LabelledIconTextField(
            labelId = R.string.salary,
            icon = ImageVector.vectorResource(id = R.drawable.attach_money),
            value = salary,
            onValueChange = onSalaryChanged,
            keyboardType = KeyboardType.Decimal,
            isError = salaryError.value,
            errorMessage = salaryErrorMessage.value
        )

        LabelledIconTextField(
            labelId = R.string.notes,
            icon = Icons.Default.Edit,
            value = note,
            onValueChange = onNoteChanged,
            singleLine = false,
            maxLines = 3,
            keyboardType = KeyboardType.Text
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
        phoneNumber = "0612345678",
        onPhoneNumberChanged = {},
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