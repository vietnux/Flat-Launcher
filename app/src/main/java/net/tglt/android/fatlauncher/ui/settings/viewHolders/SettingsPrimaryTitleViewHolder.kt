package net.tglt.android.fatlauncher.ui.settings.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import net.tglt.android.fatlauncher.R
import net.tglt.android.fatlauncher.providers.color.theme.ColorTheme
import net.tglt.android.fatlauncher.ui.home.main.tile.viewHolders.hideIfNullOr
import net.tglt.android.fatlauncher.ui.settings.SettingsItem

class SettingsPrimaryTitleViewHolder(itemView: View) : SettingsViewHolder(itemView) {

    val text = itemView.findViewById<TextView>(R.id.text)
    val description = itemView.findViewById<TextView>(R.id.description)
    val icon = itemView.findViewById<ImageView>(R.id.icon)

    override fun onBind(item: SettingsItem<*>) {
        text.text = item.text
        description.text = item.description

        text.setTextColor(ColorTheme.adjustColorForContrast(ColorTheme.cardBG, ColorTheme.secondaryAccentColor))
        icon.setImageDrawable(item.icon)

        itemView.setOnClickListener(item.onClick)

        description.hideIfNullOr(item.description) {
            text = it
            setTextColor(ColorTheme.cardDescription)
        }
    }
}