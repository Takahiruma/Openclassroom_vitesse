package com.openclassroom.vitesse.data

import java.util.Calendar

data class Candidate(
    val photo: String="",
    val firstName: String,
    val lastName: String,
    val dateOfBirth: Calendar,
    val phoneNumber: String,
    val email: String,
    val note: String,
    val salary: Double,
    val isFavorite: Boolean = false,
    val id: Int = 0,
)

