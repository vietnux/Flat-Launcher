package net.tglt.android.fatlauncher.ui.popup.appItem

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.tglt.android.fatlauncher.R

class ShortcutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val icon = itemView.findViewById<ImageView>(R.id.icon)!!
    val label = itemView.findViewById<TextView>(R.id.text)!!
}
