package com.openclassroom.vitesse.data

data class CandidateFormInput(
    val photo: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val dateOfBirthMillis: Long,
    val email: String,
    val note: String,
    val salary: String,
)
