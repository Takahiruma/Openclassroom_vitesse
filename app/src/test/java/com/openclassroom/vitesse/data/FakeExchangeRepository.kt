package com.openclassroom.vitesse.data

import com.openclassroom.vitesse.repository.ExchangeRepository

class FakeExchangeRepository : ExchangeRepository() {

    private var rates = mutableMapOf<String, Double>()
    private var shouldFail = false

    fun setRate(from: String, to: String, rate: Double) {
        rates["$from-$to"] = rate
        shouldFail = false
    }

    fun setShouldFail(fail: Boolean) {
        shouldFail = fail
    }

    override suspend fun fetchRates(currencyCode: String): ExchangeRatesResponse {
        if (shouldFail) throw Exception("Network error")
        val key = rates.keys.firstOrNull { it.startsWith(currencyCode) } ?: ""
        val target = key.substringAfter("-")
        val map: Map<String, Double> = mapOf(target to (rates[key] ?: 0.0))
        return ExchangeRatesResponse(
            date = "2024-01-01",
            rates = map
        )
    }

}
