package net.tglt.android.fatlauncher.providers.search

import android.app.Activity
import android.content.Context
import net.tglt.android.fatlauncher.LauncherContext
import net.tglt.android.fatlauncher.data.items.App
import net.tglt.android.fatlauncher.data.search.DebugResult
import net.tglt.android.fatlauncher.data.search.SearchResult
import java.util.*

class Searcher(
    val launcherContext: LauncherContext,
    vararg providers: (Searcher) -> SearchProvider,
    val update: (SearchQuery, List<SearchResult>) -> Unit
) {
    val settings by launcherContext::settings

    val providers = providers.map { it(this) }

    fun query(query: SearchQuery) {
        val r = LinkedList<SearchResult>()
        providers.flatMapTo(r) { it.getResults(query) }
        if (query.text == "!debug") {
            r += DebugResult()
        }
        r.sortWith { a, b ->
            b.relevance.compareTo(a.relevance)
        }
        val tr = if (r.size > MAX_RESULTS) r.subList(0, MAX_RESULTS) else r
        update(query, tr)
    }

    fun query(query: String?) {
        val q = query?.let(::SearchQuery) ?: SearchQuery.EMPTY
        if (query == null)
            update(q, emptyList())
        else query(q)
    }

    fun onCreate(activity: Activity) {
        providers.forEach {
            it.run { activity.onCreate() }
        }
    }

    fun onAppsLoaded(context: Context, list: List<App>) {
        providers.forEach {
            it.onAppsLoaded(context, list)
        }
    }

    companion object {
        const val MAX_RESULTS = 32
    }
}