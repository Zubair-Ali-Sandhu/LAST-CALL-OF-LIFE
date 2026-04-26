package com.example.myapplicationpractice.fragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.adapters.AlertAdapter
import com.example.myapplicationpractice.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragment that fetches and displays live safety alerts from
 * the National Weather Service API (F1: REST API Integration).
 *
 * - Network call runs on Dispatchers.IO (background thread) ✓
 * - UI updates on Dispatchers.Main ✓
 * - Data displayed in RecyclerView ✓
 * - Clicking an alert navigates to AlertDetailFragment ✓
 */
class NewsFeedFragment : Fragment(R.layout.fragment_news_feed) {

    private lateinit var adapter: AlertAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_alerts)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_loading)
        val errorLayout = view.findViewById<View>(R.id.layout_error_state)
        val errorTitle = view.findViewById<TextView>(R.id.tv_error_title)
        val errorDesc = view.findViewById<TextView>(R.id.tv_error_desc)
        val retryBtn = view.findViewById<View>(R.id.btn_retry)
        val alertCount = view.findViewById<TextView>(R.id.tv_alert_count)

        adapter = AlertAdapter { alert ->
            // Navigate to detail screen with all alert properties
            val props = alert.properties
            findNavController().navigate(
                R.id.action_newsFeed_to_alertDetail,
                bundleOf(
                    AlertDetailFragment.ARG_ALERT_ID to alert.id,
                    AlertDetailFragment.ARG_EVENT to (props.event ?: "Unknown"),
                    AlertDetailFragment.ARG_HEADLINE to (props.headline ?: "No headline"),
                    AlertDetailFragment.ARG_DESCRIPTION to (props.description ?: "No description available"),
                    AlertDetailFragment.ARG_SEVERITY to (props.severity ?: "Unknown"),
                    AlertDetailFragment.ARG_URGENCY to (props.urgency ?: "Unknown"),
                    AlertDetailFragment.ARG_STATUS to (props.status ?: "Unknown"),
                    AlertDetailFragment.ARG_AREA to (props.areaDesc ?: "—"),
                    AlertDetailFragment.ARG_SENDER to (props.senderName ?: "—"),
                    AlertDetailFragment.ARG_EFFECTIVE to (props.effective ?: "—"),
                    AlertDetailFragment.ARG_EXPIRES to (props.expires ?: "—")
                )
            )
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        retryBtn.setOnClickListener {
            fetchAlerts(recyclerView, progressBar, errorLayout, errorTitle, errorDesc, alertCount)
        }

        fetchAlerts(recyclerView, progressBar, errorLayout, errorTitle, errorDesc, alertCount)
    }

    /**
     * Fetch active alerts from NWS API on a background thread using coroutines.
     */
    private fun fetchAlerts(
        recyclerView: RecyclerView,
        progressBar: ProgressBar,
        errorLayout: View,
        errorTitle: TextView,
        errorDesc: TextView,
        alertCount: TextView
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            // Show loading state
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            errorLayout.visibility = View.GONE

            try {
                // ── Network call on background thread (Dispatchers.IO) ──
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.alertService.getActiveAlerts()
                }

                // ── Update UI on main thread ──
                val alerts = response.features
                if (alerts.isNotEmpty()) {
                    adapter.submitList(alerts)
                    recyclerView.visibility = View.VISIBLE
                    alertCount.text = alerts.size.toString()
                } else {
                    errorTitle.text = "No Active Alerts"
                    errorDesc.text = "There are currently no active safety alerts"
                    errorLayout.visibility = View.VISIBLE
                }

            } catch (e: Exception) {
                // ── Handle error ──
                errorTitle.text = "Connection Error"
                errorDesc.text = "Could not fetch alerts: ${e.localizedMessage}"
                errorLayout.visibility = View.VISIBLE
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
