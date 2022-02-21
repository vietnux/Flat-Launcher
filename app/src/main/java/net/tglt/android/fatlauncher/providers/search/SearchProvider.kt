package net.tglt.android.fatlauncher.providers.search

import android.app.Activity
import android.content.Context
import net.tglt.android.fatlauncher.data.search.SearchResult
import net.tglt.android.fatlauncher.providers.app.AppCollection

interface SearchProvider {

    fun Activity.onCreate() {}
    fun getResults(query: SearchQuery): List<SearchResult>

    fun onAppsLoaded(context: Context, apps: AppCollection) {}
}