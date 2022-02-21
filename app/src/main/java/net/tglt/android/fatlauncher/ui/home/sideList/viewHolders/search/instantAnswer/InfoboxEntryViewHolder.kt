package net.tglt.android.fatlauncher.ui.home.sideList.viewHolders.search.instantAnswer

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.tglt.android.fatlauncher.R

class InfoboxEntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val label = itemView.findViewById<TextView>(R.id.label)
    val value = itemView.findViewById<TextView>(R.id.value)
    val separator = itemView.findViewById<View>(R.id.separator)
}
