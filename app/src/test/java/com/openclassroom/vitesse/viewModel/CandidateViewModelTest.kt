package com.openclassroom.vitesse.viewModel

import com.openclassroom.vitesse.utils.MainDispatcherRule
import com.openclassroom.vitesse.data.Candidate
import com.openclassroom.vitesse.data.CandidateFakeDao
import com.openclassroom.vitesse.repository.CandidateRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class CandidateViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private fun createCandidate(id: Int, firstName: String) = Candidate(
        id = id,
        photo = "",
        firstName = firstName,
        lastName = "Dupont",
        phoneNumber = "0612345678",
        dateOfBirth = Calendar.getInstance().apply { set(1985, 5, 15) },
        email = "alice.dupont@example.com",
        note = "Experienced Kotlin developer.",
        salary = 55000.0,
        isFavorite = false
    )

    @Test
    fun testAddCandidate() = runTest {
        val fakeDao = CandidateFakeDao()
        val repository = CandidateRepository(fakeDao, ioDispatcher = testDispatcher)
        val viewModel = CandidateViewModel(repository, this)

        val candidate = createCandidate(1, "Alice")

        viewModel.addCandidate(candidate)
        advanceUntilIdle()

        val candidates = viewModel.candidates.value
        Assert.assertEquals(1, candidates.size)
        Assert.assertEquals("Alice", candidates[0].firstName)

        this.coroutineContext.cancelChildren()
    }

    @Test
    fun testRemoveCandidate() = runTest {
        val fakeDao = CandidateFakeDao()
        val repository = CandidateRepository(fakeDao, ioDispatcher = testDispatcher)
        val viewModel = CandidateViewModel(repository, this)

        val candidate = createCandidate(1, "Bob")
        viewModel.addCandidate(candidate)
        advanceUntilIdle()

        viewModel.removeCandidate(candidate)
        advanceUntilIdle()

        val candidates = viewModel.candidates.value
        Assert.assertEquals(0, candidates.size)

        this.coroutineContext.cancelChildren()
    }

    @Test
    fun testUpdateCandidate() = runTest {
        val fakeDao = CandidateFakeDao()
        val repository = CandidateRepository(fakeDao, ioDispatcher = testDispatcher)
        val viewModel = CandidateViewModel(repository, this)

        val candidate = createCandidate(1, "Charlie")
        viewModel.addCandidate(candidate)
        advanceUntilIdle()

        val updatedCandidate = candidate.copy(firstName = "Charles")
        viewModel.updateCandidate(updatedCandidate)
        advanceUntilIdle()

        val candidates = viewModel.candidates.value
        Assert.assertEquals(1, candidates.size)
        Assert.assertEquals("Charles", candidates[0].firstName)

        this.coroutineContext.cancelChildren()
    }

    @Test
    fun testAddOrUpdateCandidate_addsWhenNotExists() = runTest {
        val fakeDao = CandidateFakeDao()
        val repository = CandidateRepository(fakeDao, ioDispatcher = testDispatcher)
        val viewModel = CandidateViewModel(repository, this)

        val candidate = createCandidate(1, "Alice")
        viewModel.addOrUpdateCandidate(candidate)
        advanceUntilIdle()

        val candidates = viewModel.candidates.value
        Assert.assertEquals(1, candidates.size)
        Assert.assertEquals("Alice", candidates[0].firstName)

        this.coroutineContext.cancelChildren()
    }

    @Test
    fun testAddOrUpdateCandidate_updatesWhenExists() = runTest {
        val fakeDao = CandidateFakeDao()
        val repository = CandidateRepository(fakeDao, ioDispatcher = testDispatcher)
        val viewModel = CandidateViewModel(repository, this)

        val candidate = createCandidate(1, "Bob")
        viewModel.addCandidate(candidate)
        advanceUntilIdle()

        val updatedCandidate = candidate.copy(firstName = "Bobby")
        viewModel.addOrUpdateCandidate(updatedCandidate)
        advanceUntilIdle()

        val candidates = viewModel.candidates.value
        Assert.assertEquals(1, candidates.size)
        Assert.assertEquals("Bobby", candidates[0].firstName)

        this.coroutineContext.cancelChildren()
    }

    @Test
    fun testToggleFavorite() = runTest {
        val fakeDao = CandidateFakeDao()
        val repository = CandidateRepository(fakeDao, ioDispatcher = testDispatcher)
        val viewModel = CandidateViewModel(repository)

        val candidate = createCandidate(1, "Diana")
        viewModel.addCandidate(candidate)
        advanceUntilIdle()

        val beforeToggle = viewModel.candidates.value.getOrNull(0)
        Assert.assertNotNull("Candidate should be added", beforeToggle)
        Assert.assertEquals(false, beforeToggle?.isFavorite)

        viewModel.toggleFavorite(beforeToggle!!)
        advanceUntilIdle()

        val afterToggle = viewModel.candidates.value.getOrNull(0)
        Assert.assertNotNull("Candidate should still be present after toggle", afterToggle)
        Assert.assertEquals(true, afterToggle?.isFavorite)

    }

}