package net.tglt.android.fatlauncher.providers.search

@JvmInline
value class SearchQuery(
    val text: String
) {
    inline val length: Int get() = text.length

    override fun toString() = text

    companion object {
        val EMPTY = SearchQuery("")
    }
}
