package com.openclassroom.vitesse.repository

import com.openclassroom.vitesse.data.ExchangeRatesResponse
import com.openclassroom.vitesse.service.ExchangeApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

object RetrofitInstance {
    val api: ExchangeApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeApiService::class.java)
    }
}

open class ExchangeRepository {
    private val api = RetrofitInstance.api

    open suspend fun fetchRates(currencyCode: String): ExchangeRatesResponse = api.getRates(currencyCode)
}