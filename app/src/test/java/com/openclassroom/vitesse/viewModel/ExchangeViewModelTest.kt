package com.openclassroom.vitesse.viewModel

import com.openclassroom.vitesse.data.FakeExchangeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExchangeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ExchangeViewModel
    private lateinit var repository: FakeExchangeRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeExchangeRepository()
        viewModel = ExchangeViewModel(repository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadConversionRate_Success() = runTest {
        repository.setRate(from = "usd", to = "eur", rate = 1.2)

        val job = launch {
            viewModel.conversionRate.collect { rate ->
                if (rate == 1.2) {
                    cancel()
                }
            }
        }

        viewModel.loadConversionRate("USD", "EUR")

        job.join()

        assertEquals(1.2, viewModel.conversionRate.value, 0.001)

        this.coroutineContext.cancelChildren()
    }

    @Test
    fun testLoadConversionRate_Failure() = runTest {
        repository.setShouldFail(true)

        viewModel.loadConversionRate("USD", "EUR")

        val rate = viewModel.conversionRate.first()
        assertEquals(0.0, rate, 0.001)
    }
}