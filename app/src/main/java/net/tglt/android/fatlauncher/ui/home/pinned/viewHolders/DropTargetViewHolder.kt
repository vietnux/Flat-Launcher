package net.tglt.android.fatlauncher.ui.home.pinned.viewHolders

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import net.tglt.android.fatlauncher.R
import net.tglt.android.fatlauncher.providers.color.pallete.ColorPalette
import net.tglt.android.fatlauncher.providers.color.theme.ColorTheme
import net.tglt.android.fatlauncher.ui.home.pinned.TileArea.Companion.WIDTH_TO_HEIGHT
import net.tglt.android.fatlauncher.util.view.HorizontalAspectRatioLayout

class DropTargetViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    val aspect = itemView.findViewById<HorizontalAspectRatioLayout>(R.id.aspect)!!.apply {
        widthToHeight = WIDTH_TO_HEIGHT
    }

    init {
        itemView.background = itemView.context.getDrawable(R.drawable.tile_drop_target)
        itemView.backgroundTintMode = PorterDuff.Mode.MULTIPLY
    }
}

fun bindDropTargetViewHolder(
    holder: DropTargetViewHolder,
) {
    holder.itemView.backgroundTintList = ColorStateList.valueOf(ColorTheme.adjustColorForContrast(ColorPalette.wallColor, ColorPalette.wallColor))
}