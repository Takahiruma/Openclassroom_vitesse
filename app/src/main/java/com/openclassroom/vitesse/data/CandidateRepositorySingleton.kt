package com.openclassroom.vitesse.data

import com.openclassroom.vitesse.repository.CandidateRepository

object CandidateRepositorySingleton {
    val candidateRepository: CandidateRepository by lazy{
        CandidateRepository()
    }
}