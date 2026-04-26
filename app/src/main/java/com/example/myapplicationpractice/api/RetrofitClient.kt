package com.example.myapplicationpractice.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton Retrofit client for the NWS API.
 * Used for F1: REST API Integration.
 */
object RetrofitClient {

    private const val BASE_URL = "https://api.weather.gov/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /** Lazily-initialised NWS alert service instance. */
    val alertService: NwsAlertService by lazy {
        retrofit.create(NwsAlertService::class.java)
    }
}
