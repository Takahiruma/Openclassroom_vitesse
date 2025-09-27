package com.openclassroom.vitesse.data

fun Candidate.toEntity() = CandidateEntity(
    id = this.id,
    photo = this.photo,
    firstName = this.firstName,
    lastName = this.lastName,
    dateOfBirth = this.dateOfBirth,
    phoneNumber = this.phoneNumber,
    email = this.email,
    note = this.note,
    salary = this.salary,
    isFavorite = this.isFavorite
)

fun CandidateEntity.toDomain() = Candidate(
    id = this.id,
    photo = this.photo,
    firstName = this.firstName,
    lastName = this.lastName,
    dateOfBirth = this.dateOfBirth,
    phoneNumber = this.phoneNumber,
    email = this.email,
    note = this.note,
    salary = this.salary,
    isFavorite = this.isFavorite
)