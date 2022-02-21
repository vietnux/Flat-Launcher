package net.tglt.android.fatlauncher.ui.settings.iconPackPicker.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.tglt.android.fatlauncher.R
import net.tglt.android.fatlauncher.providers.color.theme.ColorTheme

class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val text = itemView.findViewById<TextView>(R.id.text)

    fun bind(string: String) {
        text.text = string
        text.setTextColor(ColorTheme.adjustColorForContrast(ColorTheme.uiBG, ColorTheme.accentColor))
    }
}