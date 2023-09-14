package net.tglt.android.fatlauncher.providers.search

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.willowtreeapps.fuzzywuzzy.diffutils.FuzzySearch
import net.tglt.android.fatlauncher.data.search.ContactResult
import net.tglt.android.fatlauncher.data.search.Relevance
import net.tglt.android.fatlauncher.data.search.SearchResult
import java.util.*

class ContactProvider(
    searcher: Searcher
) : SearchProvider {

    var contacts: Collection<ContactResult> = emptyList()

    override fun Activity.onCreate() {
        if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 0)
            return
        }
        contacts = ContactResult.getList(this)
    }

    override fun getResults(query: SearchQuery): List<SearchResult> {
        val results = LinkedList<SearchResult>()
        contacts.forEach {
            val r = FuzzySearch.tokenSortPartialRatio(query.toString(), it.title) / 100f * if (it.contact.isStarred) 1.1f else 1f
            if (r > .6f) {
                it.relevance = Relevance(r)
                results += it
            }
        }
        return results
    }
}