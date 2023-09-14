package net.tglt.android.fatlauncher.ui.popup.listPopup.viewHolders

import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import net.tglt.android.fatlauncher.R
import net.tglt.android.fatlauncher.providers.color.theme.ColorTheme
import net.tglt.android.fatlauncher.ui.home.pinned.viewHolders.hideIfNullOr
import net.tglt.android.fatlauncher.ui.popup.listPopup.ListPopupItem
import net.tglt.android.fatlauncher.util.drawable.FastColorDrawable

class ListPopupItemViewHolder(itemView: View) : ListPopupViewHolder(itemView) {

    val icon = itemView.findViewById<ImageView>(R.id.icon)

    val text = itemView.findViewById<TextView>(R.id.text)
    val description = itemView.findViewById<TextView>(R.id.description)

    val ripple = RippleDrawable(ColorStateList.valueOf(0), null, FastColorDrawable(0xffffffff.toInt()))

    init {
        itemView.background = ripple
    }

    override fun onBind(item: ListPopupItem) {
        text.text = item.text
        description.text = item.description

        text.setTextColor(ColorTheme.cardTitle)

        itemView.setOnClickListener(item.onClick)

        ripple.setColor(ColorStateList.valueOf(ColorTheme.accentColor and 0xffffff or 0x33000000))

        description.hideIfNullOr(item.description) {
            text = it
            setTextColor(ColorTheme.cardDescription)
        }
        icon.hideIfNullOr(item.icon) {
            setImageDrawable(it)
            imageTintList = ColorStateList.valueOf(ColorTheme.cardDescription)
        }
    }
}