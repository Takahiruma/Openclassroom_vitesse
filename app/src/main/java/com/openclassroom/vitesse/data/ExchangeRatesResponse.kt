package com.openclassroom.vitesse.data

import com.google.gson.annotations.SerializedName

typealias CurrencyList = Map<String, String>

data class ExchangeRatesResponse(
    val date: String,
    @SerializedName("eur") val rates: Map<String, Double>
)