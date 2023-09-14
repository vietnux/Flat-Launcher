package net.tglt.android.fatlauncher.ui.home.sideList.viewHolders.search.instantAnswer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.tglt.android.fatlauncher.R
import net.tglt.android.fatlauncher.providers.color.theme.ColorTheme

class InfoBoxAdapter : RecyclerView.Adapter<InfoboxEntryViewHolder>() {

    private var entries: List<Pair<String, String>> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        InfoboxEntryViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.search_result_answer_info_box_entry, parent, false))

    override fun onBindViewHolder(holder: InfoboxEntryViewHolder, i: Int) {
        val e = entries[i]
        holder.label.text = e.first
        holder.value.text = e.second

        holder.label.setTextColor(ColorTheme.cardTitle)
        holder.value.setTextColor(ColorTheme.cardDescription)
        holder.separator.setBackgroundColor(ColorTheme.separator)
    }

    override fun getItemCount() = entries.size

    fun updateEntries(entries: List<Pair<String, String>>) {
        this.entries = entries
        notifyDataSetChanged()
    }
}
