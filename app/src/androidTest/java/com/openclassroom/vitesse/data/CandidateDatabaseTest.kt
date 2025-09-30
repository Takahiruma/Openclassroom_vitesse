package com.openclassroom.vitesse.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
class CandidateDatabaseTest {

    private lateinit var db: CandidateDatabase
    private lateinit var candidateDao: CandidateDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CandidateDatabase::class.java).build()
        candidateDao = db.candidateDao()
    }

    @After
    fun tearDown() {
        db.close()
    }



    @Test
    fun insertAndGetCandidate() = runBlocking {
        val candidate = CandidateEntity(
            id = 1,
            photo = "",
            firstName = "Alice",
            lastName = "Dupont",
            phoneNumber = "0612345678",
            dateOfBirth = Calendar.getInstance().apply { set(1985, 5, 15) },
            email = "alice.dupont@example.com",
            note = "Experienced Kotlin developer.",
            salary = 55000.0,
            isFavorite = false
        )
        candidateDao.insertCandidate(candidate)
        val result = candidateDao.getAllCandidates().first()
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Alice", result[0].firstName)
    }

    @Test
    fun insertAndDeleteCandidate() = runBlocking {
        val candidate = CandidateEntity(
            id = 1,
            photo = "",
            firstName = "Jean",
            lastName = "Dupont",
            phoneNumber = "0612345678",
            dateOfBirth = Calendar.getInstance().apply { set(1985, 5, 15) },
            email = "alice.dupont@example.com",
            note = "Experienced Kotlin developer.",
            salary = 55000.0,
            isFavorite = false
        )
        candidateDao.insertCandidate(candidate)
        candidateDao.deleteCandidate(candidate)
        val result = candidateDao.getAllCandidates().first()
        Assert.assertEquals(0, result.size)
    }
}