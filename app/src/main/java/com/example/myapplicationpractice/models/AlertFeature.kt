package com.example.myapplicationpractice.models

/**
 * Models for the National Weather Service (NWS) Alerts API response.
 * API: https://api.weather.gov/alerts/active
 * Used for F1: REST API Integration.
 */

/** Top-level response from NWS /alerts/active endpoint */
data class NwsAlertResponse(
    val features: List<AlertFeature>
)

/** A single alert feature (GeoJSON feature) */
data class AlertFeature(
    val id: String,
    val properties: AlertProperties
)

/** Properties of an NWS alert */
data class AlertProperties(
    val event: String?,
    val headline: String?,
    val description: String?,
    val severity: String?,
    val urgency: String?,
    val areaDesc: String?,
    val senderName: String?,
    val effective: String?,
    val expires: String?,
    val status: String?,
    val messageType: String?
)
