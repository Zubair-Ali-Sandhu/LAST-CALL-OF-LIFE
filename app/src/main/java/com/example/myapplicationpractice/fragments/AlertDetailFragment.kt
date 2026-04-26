package com.example.myapplicationpractice.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplicationpractice.R
import com.google.android.material.appbar.MaterialToolbar

/**
 * Displays full details of a single NWS safety alert.
 * Receives all alert properties as Bundle arguments from NewsFeedFragment.
 */
class AlertDetailFragment : Fragment(R.layout.fragment_alert_detail) {

    companion object {
        const val ARG_ALERT_ID = "alert_id"
        const val ARG_EVENT = "alert_event"
        const val ARG_HEADLINE = "alert_headline"
        const val ARG_DESCRIPTION = "alert_description"
        const val ARG_SEVERITY = "alert_severity"
        const val ARG_URGENCY = "alert_urgency"
        const val ARG_STATUS = "alert_status"
        const val ARG_AREA = "alert_area"
        const val ARG_SENDER = "alert_sender"
        const val ARG_EFFECTIVE = "alert_effective"
        const val ARG_EXPIRES = "alert_expires"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialToolbar>(R.id.toolbar)?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val args = arguments ?: return

        val alertId = args.getString(ARG_ALERT_ID, "")
        val event = args.getString(ARG_EVENT, "Unknown Event")
        val headline = args.getString(ARG_HEADLINE, "No headline available")
        val description = args.getString(ARG_DESCRIPTION, "No description available")
        val severity = args.getString(ARG_SEVERITY, "Unknown")
        val urgency = args.getString(ARG_URGENCY, "Unknown")
        val status = args.getString(ARG_STATUS, "Unknown")
        val area = args.getString(ARG_AREA, "—")
        val sender = args.getString(ARG_SENDER, "—")
        val effective = args.getString(ARG_EFFECTIVE, "—")
        val expires = args.getString(ARG_EXPIRES, "—")

        // Populate views
        view.findViewById<TextView>(R.id.tv_detail_event).text = event
        view.findViewById<TextView>(R.id.tv_detail_headline).text = headline
        view.findViewById<TextView>(R.id.tv_detail_description).text = description
        view.findViewById<TextView>(R.id.tv_detail_urgency).text = urgency
        view.findViewById<TextView>(R.id.tv_detail_status).text = status
        view.findViewById<TextView>(R.id.tv_detail_area).text = area
        view.findViewById<TextView>(R.id.tv_detail_sender).text = "Issued by: $sender"

        // Format date strings (take just the date/time part)
        view.findViewById<TextView>(R.id.tv_detail_effective).text = formatDate(effective)
        view.findViewById<TextView>(R.id.tv_detail_expires).text = formatDate(expires)

        // Severity badge with color
        val severityView = view.findViewById<TextView>(R.id.tv_detail_severity)
        severityView.text = severity.uppercase()
        val severityColor = when (severity.lowercase()) {
            "extreme" -> requireContext().getColor(R.color.accent_red)
            "severe" -> requireContext().getColor(R.color.accent_orange)
            "moderate" -> requireContext().getColor(R.color.accent_warning)
            else -> requireContext().getColor(R.color.accent_safe)
        }
        severityView.setTextColor(severityColor)

        // Open in browser button
        view.findViewById<View>(R.id.btn_open_browser).setOnClickListener {
            if (alertId.isNotBlank()) {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(alertId)))
                } catch (_: Exception) { }
            }
        }
    }

    /**
     * Simplify ISO date string for display.
     * "2026-04-26T03:55:00-05:00" → "Apr 26, 03:55"
     */
    private fun formatDate(isoDate: String): String {
        if (isoDate == "—" || isoDate.isBlank()) return "—"
        return try {
            // Extract date and time parts
            val parts = isoDate.split("T")
            if (parts.size < 2) return isoDate
            val datePart = parts[0] // 2026-04-26
            val timePart = parts[1].substringBefore("-").substringBefore("+") // 03:55:00

            val datePieces = datePart.split("-")
            val months = arrayOf(
                "", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
            )
            val month = months.getOrElse(datePieces[1].toInt()) { "?" }
            val day = datePieces[2].toInt()
            val time = timePart.substring(0, 5) // HH:mm
            "$month $day, $time"
        } catch (_: Exception) {
            isoDate.take(16)
        }
    }
}
