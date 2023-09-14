package net.tglt.android.fatlauncher.providers.search

import net.tglt.android.fatlauncher.data.search.SearchResult

abstract class AsyncSearchProvider(
    private val searcher: Searcher
) : SearchProvider {

    private val lastResults = HashMap<SearchQuery, List<SearchResult>>()
    private var lastQuery: SearchQuery? = null

    override fun getResults(query: SearchQuery): List<SearchResult> {
        return lastResults.getOrElse(query) {
            lastQuery = query
            loadResults(query)
            emptyList()
        }
    }

    abstract fun loadResults(query: SearchQuery)

    fun update(query: SearchQuery, results: List<SearchResult>) {
        lastResults[query] = results
        if (lastQuery == query)
            searcher.query(query)
    }
}