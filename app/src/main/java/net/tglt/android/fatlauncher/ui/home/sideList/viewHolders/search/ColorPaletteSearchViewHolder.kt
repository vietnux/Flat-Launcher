package net.tglt.android.fatlauncher.ui.home.sideList.viewHolders.search

import android.app.WallpaperManager
import android.os.Build
import android.view.View
import androidx.core.view.isVisible
import net.tglt.android.fatlauncher.R
import net.tglt.android.fatlauncher.data.search.DebugResult
import net.tglt.android.fatlauncher.data.search.SearchResult
import net.tglt.android.fatlauncher.providers.color.pallete.ColorPalette
import net.tglt.android.fatlauncher.ui.home.MainActivity
import net.tglt.android.fatlauncher.ui.home.main.acrylicBlur
import net.tglt.android.fatlauncher.ui.home.sideList.viewHolders.search.instantAnswer.AnswerSearchViewHolder
import net.tglt.android.fatlauncher.ui.view.ColorPaletteTestView
import net.tglt.android.fatlauncher.ui.view.SeeThroughView

class ColorPaletteSearchViewHolder(
    itemView: View
) : SearchViewHolder(itemView) {

    private val paletteViews = arrayOf<ColorPaletteTestView>(
        itemView.findViewById(R.id.palette_view_0)!!,
        itemView.findViewById(R.id.palette_view_1)!!,
        itemView.findViewById(R.id.palette_view_2)!!,
        itemView.findViewById(R.id.palette_view_3)!!,
    )

    private val blurBG = itemView.findViewById<SeeThroughView>(R.id.blur_bg)!!.apply {
        viewTreeObserver.addOnPreDrawListener {
            invalidate()
            true
        }
    }

    override fun onBind(
        result: SearchResult,
        activity: MainActivity,
    ) {
        result as DebugResult

        blurBG.drawable = acrylicBlur?.smoothBlurDrawable
        blurBG.offset = 1f
        activity.setOnPageScrollListener(AnswerSearchViewHolder::class.simpleName!!) { blurBG.offset = it }

        paletteViews[0].palette = ColorPalette.getDefaultColorPalette()
        paletteViews[1].palette = ColorPalette.getWallColorPalette(itemView.context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            val wallpaperManager = WallpaperManager.getInstance(itemView.context)
            paletteViews[2].palette = ColorPalette.getSystemWallColorPalette(
                itemView.context,
                wallpaperManager.getWallpaperColors(WallpaperManager.FLAG_SYSTEM)!!
            )
            paletteViews[2].isVisible = true
        } else {
            paletteViews[2].isVisible = false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            paletteViews[3].palette = ColorPalette.getMonetColorPalette(itemView.context)
            paletteViews[3].isVisible = true
        } else {
            paletteViews[3].isVisible = false
        }
    }
}