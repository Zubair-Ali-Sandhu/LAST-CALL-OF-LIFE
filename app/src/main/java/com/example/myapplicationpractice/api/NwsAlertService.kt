package com.example.myapplicationpractice.api

import com.example.myapplicationpractice.models.NwsAlertResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Retrofit service interface for the National Weather Service (NWS) Alerts API.
 * Base URL: https://api.weather.gov/
 * No API key required — just a User-Agent header.
 *
 * Used for F1: REST API Integration.
 */
interface NwsAlertService {

    /**
     * Fetch active weather/safety alerts.
     * Uses /alerts?active=true endpoint (the /alerts/active path returns 400).
     * @param active  true to get only active alerts
     * @param limit   max number of alerts to return
     */
    @Headers(
        "User-Agent: LastCallOfLife-Android-App (lastcall@example.com)",
        "Accept: application/geo+json"
    )
    @GET("alerts")
    suspend fun getActiveAlerts(
        @Query("active") active: Boolean = true,
        @Query("limit") limit: Int = 20
    ): NwsAlertResponse
}
