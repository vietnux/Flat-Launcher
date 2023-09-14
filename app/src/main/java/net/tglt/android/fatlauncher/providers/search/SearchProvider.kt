package net.tglt.android.fatlauncher.providers.search

import android.app.Activity
import android.content.Context
import net.tglt.android.fatlauncher.data.items.App
import net.tglt.android.fatlauncher.data.search.SearchResult

interface SearchProvider {

    fun Activity.onCreate() {}
    fun getResults(query: SearchQuery): List<SearchResult>

    fun onAppsLoaded(context: Context, list: List<App>) {}

    companion object {
        fun matchInitials(query: String, string: String): Boolean {
            val initials = string.split(Regex("([ .\\-_]|([a-z](?=[A-Z0-9])))")).mapNotNull(String::firstOrNull).joinToString("")
            val initialsBasic = string.split(Regex("[ .\\-_]")).mapNotNull(String::firstOrNull).joinToString("")
            return initials.startsWith(query, ignoreCase = true) || initialsBasic.startsWith(query, ignoreCase = true)
        }
    }
}