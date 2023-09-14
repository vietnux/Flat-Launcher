package net.tglt.android.fatlauncher.ui.settings.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import net.tglt.android.fatlauncher.ui.settings.SettingsItem

abstract class SettingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun onBind(item: SettingsItem<*>)
}