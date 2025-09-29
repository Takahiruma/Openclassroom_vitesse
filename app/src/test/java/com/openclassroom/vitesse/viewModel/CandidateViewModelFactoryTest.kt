package com.openclassroom.vitesse.viewModel

import androidx.lifecycle.ViewModel
import com.openclassroom.vitesse.data.Candidate
import com.openclassroom.vitesse.data.CandidateEntity
import com.openclassroom.vitesse.data.CandidateFakeDao
import com.openclassroom.vitesse.repository.CandidateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.Calendar

@ExperimentalCoroutinesApi
class CandidateViewModelFactoryTest {

    private lateinit var fakeDao: CandidateFakeDao
    private lateinit var repository: CandidateRepository
    private lateinit var factory: CandidateViewModelFactory
    private val mainDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        fakeDao = CandidateFakeDao()
        repository = CandidateRepository(fakeDao)
        factory = CandidateViewModelFactory(repository)
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(mainDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `create returns CandidateViewModel when requested`() {
        val viewModel = factory.create(CandidateViewModel::class.java)
        assertNotNull(viewModel)
        assertTrue(viewModel is CandidateViewModel)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create throws IllegalArgumentException for unknown ViewModel class`() {
        factory.create(DummyViewModel::class.java)
    }

    @Test
    fun `CandidateFakeDao emits inserted candidate`() = runBlocking {
        val candidate = Candidate(
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

        repository.addCandidate(candidate)

        val candidates = repository.candidates.first()
        assertTrue(candidates.any { it.id == candidate.id && it.firstName == "Jean" })
    }
}
