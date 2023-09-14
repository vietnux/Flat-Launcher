package net.tglt.android.fatlauncher.ui.home.main.tile.viewHolders

import android.app.Activity
import android.content.pm.LauncherApps
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.posidon.android.conveniencelib.getNavigationBarHeight
import net.tglt.android.fatlauncher.R
import net.tglt.android.fatlauncher.data.items.App
import net.tglt.android.fatlauncher.data.items.LauncherItem
import net.tglt.android.fatlauncher.providers.color.theme.ColorTheme
import net.tglt.android.fatlauncher.providers.item.GraphicsLoader
import net.tglt.android.fatlauncher.ui.home.main.tile.ShortcutAdapter
import net.tglt.android.fatlauncher.ui.popup.appItem.ItemLongPress
import net.tglt.android.fatlauncher.ui.view.recycler.RecyclerViewLongPressHelper
import net.tglt.android.fatlauncher.util.storage.Settings

class ShortcutTileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), TileViewHolder {

    private val card = itemView.findViewById<CardView>(R.id.card)!!

    private val shortcutsRecycler = itemView.findViewById<RecyclerView>(R.id.shortcuts)!!.apply {
        layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
    }

    override fun bind(
        item: LauncherItem,
        activity: Activity,
        settings: Settings,
        graphicsLoader: GraphicsLoader,
        onDragStart: (View) -> Unit,
    ) {
        itemView.isVisible = true
        card.setCardBackgroundColor(ColorTheme.cardBG)

        val shortcuts = listOf(item) + (item as? App)?.let {
            val l = activity.getSystemService(LauncherApps::class.java)
            val s = it.getStaticShortcuts(l)
            if (s.size > 3) s.subList(0, 3)
            else if (s.size == 3) s
            else (s + it.getDynamicShortcuts(l)).let { it.subList(0, it.size.coerceAtMost(3)) }
        }.orEmpty()

        val shortcutsAdapter = ShortcutAdapter(shortcuts, graphicsLoader, settings) {
            onLongPress(itemView, item, activity, graphicsLoader, onDragStart)
        }
        shortcutsRecycler.adapter = shortcutsAdapter

        RecyclerViewLongPressHelper.setOnLongPressListener(shortcutsRecycler) { v ->
            onLongPress(itemView, item, activity, graphicsLoader, onDragStart)
        }

        itemView.setOnLongClickListener { v ->
            onLongPress(v, item, activity, graphicsLoader, onDragStart)
            true
        }
    }

    fun onLongPress(
        v: View,
        item: LauncherItem,
        activity: Activity,
        graphicsLoader: GraphicsLoader,
        onDragStart: (View) -> Unit,
    ) {
        if (item is App) {
            val color = graphicsLoader.load(itemView.context, item).extra.color
            val backgroundColor = ColorTheme.tintCard(color)
            ItemLongPress.onItemLongPress(
                v,
                backgroundColor,
                ColorTheme.titleColorForBG(backgroundColor),
                item,
                activity.getNavigationBarHeight(),
                graphicsLoader,
            )
        } else ItemLongPress.onItemLongPress(v, item)
        onDragStart(v)
    }

    override fun recycle() {}
}