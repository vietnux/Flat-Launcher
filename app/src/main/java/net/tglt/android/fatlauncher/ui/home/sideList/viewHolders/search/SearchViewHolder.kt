package net.tglt.android.fatlauncher.ui.home.sideList.viewHolders.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import net.tglt.android.fatlauncher.data.search.SearchResult
import net.tglt.android.fatlauncher.ui.home.MainActivity

abstract class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun onBind(result: SearchResult, activity: MainActivity)
    open fun recycle(result: SearchResult) {}
}