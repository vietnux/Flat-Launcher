package net.tglt.android.fatlauncher.data.search

import android.view.View

class DebugResult : SearchResult {
    override var relevance = Relevance(1f)

    override val title: String = "Debug"

    override fun open(view: View) {}
}