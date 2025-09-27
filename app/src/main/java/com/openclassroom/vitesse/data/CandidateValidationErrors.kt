package com.openclassroom.vitesse.data

import androidx.compose.runtime.MutableState

data class CandidateValidationErrors(
    val firstNameError: MutableState<Boolean>,
    val lastNameError: MutableState<Boolean>,
    val phoneNumberError: MutableState<Boolean>,
    val emailError: MutableState<Boolean>,
    val salaryError: MutableState<Boolean>,
    val dateOfBirthError: MutableState<Boolean>,
    val firstNameErrorMessage: MutableState<String>,
    val lastNameErrorMessage: MutableState<String>,
    val phoneNumberErrorMessage: MutableState<String>,
    val emailErrorMessage: MutableState<String>,
    val salaryErrorMessage: MutableState<String>,
    val dateOfBirthErrorMessage: MutableState<String>,
)
