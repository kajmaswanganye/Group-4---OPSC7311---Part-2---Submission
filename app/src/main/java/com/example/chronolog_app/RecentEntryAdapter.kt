package com.example.chronolog_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecentEntryAdapter(private val entries: List<HomeFragment.Entry>) :
    RecyclerView.Adapter<RecentEntryAdapter.EntryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_entry, parent, false)
        return EntryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = entries[position]
        holder.bind(entry)
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val tvTask: TextView = itemView.findViewById(R.id.tvTask)

        fun bind(entry: HomeFragment.Entry) {
            tvDate.text = entry.date
            tvCategory.text = entry.category
            tvTask.text = entry.task
        }
    }
}
