package net.tglt.android.fatlauncher.ui.home.sideList.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.tglt.android.fatlauncher.R
import net.tglt.android.fatlauncher.providers.color.theme.ColorTheme

class TitleViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    val text = itemView.findViewById<TextView>(R.id.text)!!

    fun onBind(title: String) {
        text.text = title
        text.setTextColor(ColorTheme.uiTitle)
    }
}