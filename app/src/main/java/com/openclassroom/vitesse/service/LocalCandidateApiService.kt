package com.openclassroom.vitesse.service

import com.openclassroom.vitesse.data.Candidate

class LocalCandidateApiService : CandidateApiService {

    private val candidates = mutableListOf<Candidate>()

    override fun addCandidate(candidate: Candidate) {
        candidates.add(candidate)
    }

    override fun getAllCandidates(): List<Candidate> {
        return candidates.toList()
    }

    override fun removeCandidate(candidate: Candidate) {
        candidates.remove(candidate)
    }
}
