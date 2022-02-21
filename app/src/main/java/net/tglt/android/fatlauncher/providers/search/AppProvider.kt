package net.tglt.android.fatlauncher.providers.search

import android.app.Activity
import android.content.Context
import android.content.pm.LauncherApps
import android.content.res.Resources
import com.willowtreeapps.fuzzywuzzy.diffutils.FuzzySearch
import io.posidon.android.computable.Computable
import net.tglt.android.fatlauncher.data.items.App
import net.tglt.android.fatlauncher.data.search.AppResult
import net.tglt.android.fatlauncher.data.search.Relevance
import net.tglt.android.fatlauncher.data.search.SearchResult
import net.tglt.android.fatlauncher.data.search.ShortcutResult
import net.tglt.android.fatlauncher.providers.app.AppCollection
import net.tglt.android.fatlauncher.providers.suggestions.SuggestionsManager
import net.tglt.android.fatlauncher.util.drawable.NonDrawable
import java.util.*
import kotlin.concurrent.thread
import kotlin.math.pow

class AppProvider(
    val searcher: Searcher
) : SearchProvider {

    var appList = emptyList<App>()
    var staticShortcuts = emptyList<ShortcutResult>()
    var dynamicShortcuts = emptyList<ShortcutResult>()

    private lateinit var launcherApps: LauncherApps

    override fun Activity.onCreate() {
        launcherApps = getSystemService(LauncherApps::class.java)
        updateAppCache(resources, searcher.launcherContext.appManager.apps)
    }

    private fun updateAppCache(resources: Resources, list: List<App>) {
        appList = list
        thread(isDaemon = true) {
            staticShortcuts = appList.flatMap { app ->
                app.getStaticShortcuts(launcherApps).map {
                    ShortcutResult(
                        it,
                        (it.longLabel ?: it.shortLabel).toString(),
                        Computable {
                            launcherApps.getShortcutIconDrawable(
                                it,
                                resources.displayMetrics.densityDpi
                            ) ?: NonDrawable()
                        },
                        app
                    )
                }
            }
            dynamicShortcuts = appList.flatMap { app ->
                app.getDynamicShortcuts(launcherApps).map {
                    ShortcutResult(
                        it,
                        (it.longLabel ?: it.shortLabel).toString(),
                        Computable {
                            launcherApps.getShortcutIconDrawable(
                                it,
                                resources.displayMetrics.densityDpi
                            ) ?: NonDrawable()
                        },
                        app
                    )
                }
            }
        }
    }

    override fun onAppsLoaded(context: Context, apps: AppCollection) {
        updateAppCache(context.resources, apps.list)
    }

    override fun getResults(query: SearchQuery): List<SearchResult> {
        val results = LinkedList<SearchResult>()
        val suggestions = SuggestionsManager.getPatternBasedSuggestions().let { it.subList(0, it.size.coerceAtMost(6)) }
        val queryString = query.toString()
        appList.forEach {
            val i = suggestions.indexOf(it)
            val suggestionFactor = if(i == -1) 0f else (suggestions.size - i).toFloat() / suggestions.size
            val pr = run {
                val r = FuzzySearch.tokenSortPartialRatio(queryString, it.packageName) / 100f
                r * r * r * 0.8f
            }
            val r = FuzzySearch.tokenSortPartialRatio(queryString, it.label) / 100f + suggestionFactor * 0.5f + pr
            if (r > .8f) {
                results += AppResult(it).apply {
                    relevance = Relevance(r.coerceAtLeast(0.98f))
                }
            }
        }
        staticShortcuts.forEach {
            val l = FuzzySearch.tokenSortPartialRatio(queryString, it.title) / 100f
            val a = FuzzySearch.tokenSortPartialRatio(queryString, it.app.label) / 100f
            val r = (a * a * .5f + l * l).pow(.2f)
            if (r > .95f) {
                it.relevance = Relevance(l)
                results += it
            }
        }
        dynamicShortcuts.forEach {
            val l = FuzzySearch.tokenSortPartialRatio(queryString, it.title) / 100f
            val a = FuzzySearch.tokenSortPartialRatio(queryString, it.app.label) / 100f
            val r = (a * a * .2f + l * l).pow(.3f)
            if (r > .9f) {
                it.relevance = Relevance(if (l >= .95) r.coerceAtLeast(0.98f) else r.coerceAtMost(0.9f))
                results += it
            }
        }
        return results
    }
}