package com.example.myapplicationpractice.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.models.VaultModule

class VaultModuleAdapter(
    private val onItemClick: (VaultModule) -> Unit
) : RecyclerView.Adapter<VaultModuleAdapter.ModuleViewHolder>() {

    private val items = mutableListOf<VaultModule>()

    fun submitList(data: List<VaultModule>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vault_module, parent, false)
        return ModuleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModuleViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int = items.size

    class ModuleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val iconView: ImageView = itemView.findViewById(R.id.iv_module_icon)
        private val lockStateIcon: ImageView = itemView.findViewById(R.id.iv_lock_state)
        private val titleText: TextView = itemView.findViewById(R.id.tv_module_name)
        private val descText: TextView = itemView.findViewById(R.id.tv_module_description)
        private val metaText: TextView = itemView.findViewById(R.id.tv_last_updated)

        fun bind(module: VaultModule) {
            iconView.setImageResource(module.iconRes)
            titleText.text = module.title
            descText.text = module.description

            if (module.isLocked) {
                lockStateIcon.setImageResource(R.drawable.ic_vault_lock)
                lockStateIcon.setColorFilter(itemView.context.getColor(R.color.accent_warning))
                metaText.text = "Locked"
                metaText.setTextColor(itemView.context.getColor(R.color.accent_warning))
            } else {
                lockStateIcon.setImageResource(R.drawable.ic_check_circle)
                lockStateIcon.setColorFilter(itemView.context.getColor(R.color.accent_safe))
                metaText.text = "${module.itemCount} items configured"
                metaText.setTextColor(itemView.context.getColor(R.color.accent_red))
            }
        }
    }
}
