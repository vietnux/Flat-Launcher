package net.tglt.android.fatlauncher.data.search

import android.app.Activity
import android.view.View
import net.tglt.android.fatlauncher.data.items.LauncherItem
import net.tglt.android.fatlauncher.providers.item.GraphicsLoader

abstract class CompactResult : SearchResult {
    abstract val launcherItem: LauncherItem
    abstract val subtitle: String?
    override var relevance = Relevance(0f)
    abstract val onLongPress: ((GraphicsLoader, View, Activity) -> Boolean)?
}