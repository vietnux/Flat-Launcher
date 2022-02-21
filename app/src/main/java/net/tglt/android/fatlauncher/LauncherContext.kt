package net.tglt.android.fatlauncher

import android.content.Context
import io.posidon.android.launcherutils.appLoading.AppLoader
import io.posidon.android.launcherutils.appLoading.IconConfig
import net.tglt.android.fatlauncher.data.items.App
import net.tglt.android.fatlauncher.data.items.LauncherItem
import net.tglt.android.fatlauncher.providers.app.AppCollection
import net.tglt.android.fatlauncher.providers.suggestions.SuggestionsManager
import net.tglt.android.fatlauncher.util.storage.Settings
import net.tglt.android.fatlauncher.util.view.tile.TileContentMover.Companion.calculateBigIconSize

class LauncherContext {

    val settings = Settings()
    val suggestionData = Settings("stats")

    val appManager = AppManager()

    inner class AppManager {

        val pinnedItems: List<LauncherItem> get() = _pinnedItems

        var apps = emptyList<App>()
            private set

        fun <T : Context> loadApps(context: T, onEnd: T.(apps: AppCollection) -> Unit) {
            val iconConfig = IconConfig(
                size = calculateBigIconSize(context).toInt(),
                density = context.resources.configuration.densityDpi,
                packPackages = settings.getStrings("icon_packs") ?: emptyArray(),
            )

            appLoader.async(context, iconConfig) {
                apps = it.list
                appsByName = it.byName
                _pinnedItems = settings.getStrings(PINNED_KEY)?.mapNotNull { LauncherItem.tryParse(it, appsByName, context) }?.toMutableList() ?: ArrayList()
                SuggestionsManager.onAppsLoaded(this, context, suggestionData)
                onEnd(context, it)
            }
        }

        fun tryParseLauncherItem(string: String, context: Context): LauncherItem? {
            return LauncherItem.tryParse(string, appsByName, context)
        }

        fun tryParseApp(string: String): App? {
            return App.tryParse(string, appsByName)
        }

        fun getAppByPackage(packageName: String): LauncherItem? = appsByName[packageName]?.first()

        fun pinItem(context: Context, launcherItem: LauncherItem, i: Int) {
            _pinnedItems.add(i, launcherItem)
            settings.edit(context) {
                val s = launcherItem.toString()
                PINNED_KEY set (settings.getStrings(PINNED_KEY)
                    ?.toMutableList()
                    ?.apply { add(i, s) }
                    ?.toTypedArray()
                    ?: arrayOf(s))
            }
        }

        fun unpinItem(context: Context, i: Int) {
            _pinnedItems.removeAt(i)
            settings.edit(context) {
                PINNED_KEY set (settings.getStrings(PINNED_KEY)
                    ?.toMutableList()
                    ?.apply { removeAt(i) }
                    ?.toTypedArray()
                    ?: throw IllegalStateException("Can't unpin an item when no items are pinned"))
            }
        }

        fun setPinned(context: Context, pinned: List<LauncherItem>) {
            _pinnedItems = pinned.toMutableList()
            settings.edit(context) {
                PINNED_KEY set pinned.map(LauncherItem::toString).toTypedArray()
            }
        }

        private val appLoader = AppLoader { AppCollection(it, settings) }

        private var appsByName = HashMap<String, MutableList<App>>()

        private var _pinnedItems: MutableList<LauncherItem> = ArrayList()
    }

    companion object {
        private const val PINNED_KEY = "pinned_items"
    }
}