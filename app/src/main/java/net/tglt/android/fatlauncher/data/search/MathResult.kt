package net.tglt.android.fatlauncher.data.search

import android.view.View
import net.tglt.android.fatlauncher.providers.search.SearchQuery

class MathResult(
    query: SearchQuery,
    val operation: String,
    val result: String,
) : SearchResult {
    override var relevance = Relevance(2.0f)

    override val title = operation

    override fun open(view: View) {
    }
}