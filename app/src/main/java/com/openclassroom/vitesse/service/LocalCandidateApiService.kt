package com.openclassroom.vitesse.service

import com.openclassroom.vitesse.data.Candidate
import com.openclassroom.vitesse.data.CandidateData

class LocalCandidateApiService : CandidateApiService {

    private val candidates = mutableListOf<Candidate>()

    override fun init(){
        candidates.addAll(CandidateData.Candidates)
    }

    override fun addCandidate(candidate: Candidate) {
        candidates.add(candidate)
    }

    override fun getAllCandidates(): List<Candidate> {
        return candidates.toList()
    }

    override fun removeCandidate(candidate: Candidate) {
        candidates.remove(candidate)
    }

    override fun updateCandidate(candidate: Candidate) {
        val index = candidates.indexOfFirst { it.id == candidate.id }
        if (index != -1) {
            candidates[index] = candidate
        } else {
            candidates.add(candidate)
        }
    }
}
