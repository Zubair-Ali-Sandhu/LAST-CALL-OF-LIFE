package com.example.myapplicationpractice.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.models.AlertFeature

/**
 * RecyclerView adapter for displaying NWS safety alerts (F1).
 * Each item shows event name, headline, severity, area, and sender.
 * Tapping an item triggers the onItemClick callback to navigate to detail.
 */
class AlertAdapter(
    private val onItemClick: (AlertFeature) -> Unit
) : RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {

    private val items = mutableListOf<AlertFeature>()

    fun submitList(data: List<AlertFeature>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alert, parent, false)
        return AlertViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val alert = items[position]
        holder.bind(alert)
        holder.itemView.setOnClickListener { onItemClick(alert) }
    }

    override fun getItemCount(): Int = items.size

    class AlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvEvent: TextView = itemView.findViewById(R.id.tv_alert_event)
        private val tvHeadline: TextView = itemView.findViewById(R.id.tv_alert_headline)
        private val tvSeverity: TextView = itemView.findViewById(R.id.tv_alert_severity)
        private val tvArea: TextView = itemView.findViewById(R.id.tv_alert_area)
        private val tvSender: TextView = itemView.findViewById(R.id.tv_alert_sender)

        fun bind(alert: AlertFeature) {
            val props = alert.properties
            tvEvent.text = props.event ?: "Unknown Event"
            tvHeadline.text = props.headline ?: "No headline available"
            tvSeverity.text = props.severity ?: "—"
            tvArea.text = props.areaDesc ?: "—"
            tvSender.text = props.senderName ?: "—"

            // Color-code severity
            val severityColor = when (props.severity?.lowercase()) {
                "extreme" -> itemView.context.getColor(R.color.accent_red)
                "severe" -> itemView.context.getColor(R.color.accent_orange)
                "moderate" -> itemView.context.getColor(R.color.accent_warning)
                else -> itemView.context.getColor(R.color.accent_safe)
            }
            tvSeverity.setTextColor(severityColor)
        }
    }
}
