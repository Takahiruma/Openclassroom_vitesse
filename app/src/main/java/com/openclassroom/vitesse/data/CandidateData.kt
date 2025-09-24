package com.openclassroom.vitesse.data

import java.util.Calendar

object CandidateData {
    val tempCandidate = Candidate(photo = "", firstName = "Jean", lastName =  "Luc", createCalendar(2020, 2, 1),"0612345678", "Ex@email.com", "Notes with a bit of text innit", 500.0)
    val tempCandidate2 = Candidate(photo = "", firstName = "Kevin", lastName =  "Marlo", createCalendar(2020, 2, 1),"0692342377", "Ex@email.com", "Notes with a bit of text innit", 500.0)
    val Candidates: MutableList<Candidate> = mutableListOf(tempCandidate,tempCandidate2)

    fun createCalendar(year: Int, month: Int, day: Int): Calendar {
        return Calendar.getInstance().apply {
            set(year, month - 1, day)
        }
    }

    fun findCandidateById(candidateId: String) : Candidate? =
        Candidates.firstOrNull { it.id.toString() == candidateId }
}