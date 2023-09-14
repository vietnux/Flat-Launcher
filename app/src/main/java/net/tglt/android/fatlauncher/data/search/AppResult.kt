package net.tglt.android.fatlauncher.data.search

import android.app.Activity
import android.view.View
import io.posidon.android.conveniencelib.getNavigationBarHeight
import net.tglt.android.fatlauncher.data.items.App
import net.tglt.android.fatlauncher.providers.color.theme.ColorTheme
import net.tglt.android.fatlauncher.providers.item.GraphicsLoader
import net.tglt.android.fatlauncher.ui.popup.appItem.ItemLongPress

class AppResult(
    val app: App
) : CompactResult() {

    override val launcherItem get() = app

    inline val packageName: String get() = app.packageName
    inline val name: String get() = app.name
    override val title: String get() = app.label

    override val subtitle = null

    override var relevance = Relevance(0f)
    override val onLongPress = { graphicsLoader: GraphicsLoader, v: View, activity: Activity ->
        val color = graphicsLoader.load(v.context, app).extra.color
        val backgroundColor = ColorTheme.tintCard(color)
        ItemLongPress.onItemLongPress(
            v,
            backgroundColor,
            ColorTheme.titleColorForBG(backgroundColor),
            app,
            activity.getNavigationBarHeight(),
            graphicsLoader,
        )
        true
    }

    override fun open(view: View) {
        app.open(view.context, view)
    }
}