package com.openclassroom.vitesse.service

import com.openclassroom.vitesse.data.CurrencyList
import com.openclassroom.vitesse.data.ExchangeRatesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeApiService {
    @GET("currencies.json")
    suspend fun getCurrencies(): CurrencyList

    @GET("currencies/{currencyCode}.json")
    suspend fun getRates(@Path("currencyCode") currencyCode: String): ExchangeRatesResponse
}