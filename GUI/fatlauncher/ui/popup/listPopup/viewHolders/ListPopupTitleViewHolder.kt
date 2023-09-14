package net.tglt.android.fatlauncher.ui.popup.listPopup.viewHolders

import android.view.View
import android.widget.TextView
import net.tglt.android.fatlauncher.R
import net.tglt.android.fatlauncher.providers.color.theme.ColorTheme
import net.tglt.android.fatlauncher.ui.home.pinned.viewHolders.hideIfNullOr
import net.tglt.android.fatlauncher.ui.popup.listPopup.ListPopupItem

class ListPopupTitleViewHolder(itemView: View) : ListPopupViewHolder(itemView) {

    val text = itemView.findViewById<TextView>(R.id.text)
    val description = itemView.findViewById<TextView>(R.id.description)
    val separator = itemView.findViewById<View>(R.id.separator)

    override fun onBind(item: ListPopupItem) {
        text.text = item.text
        description.text = item.description

        text.setTextColor(ColorTheme.adjustColorForContrast(ColorTheme.cardBG, ColorTheme.accentColor))
        separator.setBackgroundColor(ColorTheme.cardHint)

        itemView.setOnClickListener(item.onClick)

        description.hideIfNullOr(item.description) {
            text = it
            setTextColor(ColorTheme.cardDescription)
        }
    }
}