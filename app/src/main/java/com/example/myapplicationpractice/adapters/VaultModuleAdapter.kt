package com.example.myapplicationpractice.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.models.VaultModule
import com.google.android.material.chip.Chip

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
        private val titleText: TextView = itemView.findViewById(R.id.tv_module_title)
        private val descText: TextView = itemView.findViewById(R.id.tv_module_desc)
        private val statusChip: Chip = itemView.findViewById(R.id.chip_module_status)
        private val chevron: ImageView = itemView.findViewById(R.id.iv_chevron)

        fun bind(module: VaultModule) {
            iconView.setImageResource(module.iconRes)
            titleText.text = module.title
            descText.text = module.description

            if (module.isLocked) {
                statusChip.text = "Locked"
                statusChip.setChipBackgroundColorResource(R.color.bg_card)
                chevron.alpha = 0.3f
            } else {
                statusChip.text = "${module.itemCount} items"
                chevron.alpha = 1.0f
            }
        }
    }
}
