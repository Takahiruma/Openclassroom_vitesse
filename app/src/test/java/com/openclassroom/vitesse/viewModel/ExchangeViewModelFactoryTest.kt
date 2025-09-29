package com.openclassroom.vitesse.viewModel

import androidx.lifecycle.ViewModel
import com.openclassroom.vitesse.data.FakeExchangeRepository
import org.junit.Assert.*
import org.junit.Test
import kotlinx.coroutines.Dispatchers
import org.junit.Before

class ExchangeViewModelFactoryTest {

    private lateinit var repository: FakeExchangeRepository
    private lateinit var factory: ExchangeViewModelFactory

    @Before
    fun setup() {
        repository = FakeExchangeRepository()
        factory = ExchangeViewModelFactory(repository, Dispatchers.Unconfined)
    }

    @Test
    fun `create returns ExchangeViewModel when requested`() {
        val viewModel = factory.create(ExchangeViewModel::class.java)
        assertNotNull(viewModel)
        assertTrue(viewModel is ExchangeViewModel)
    }

    @Test
    fun `create throws IllegalArgumentException for unknown ViewModel class`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            factory.create(DummyViewModel::class.java)
        }
        assertEquals("Unknown ViewModel class", exception.message)
    }
}
