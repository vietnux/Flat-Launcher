package net.tglt.android.fatlauncher.ui.home.pinned.suggestion

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import net.tglt.android.fatlauncher.R
import net.tglt.android.fatlauncher.data.items.LauncherItem
import net.tglt.android.fatlauncher.util.storage.Settings
import posidon.android.conveniencelib.getNavigationBarHeight

class SuggestionsAdapter(
    val activity: Activity,
    val settings: Settings,
) : RecyclerView.Adapter<SuggestionViewHolder>() {

    private var items: List<LauncherItem> = emptyList()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        return SuggestionViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.suggestion, parent, false) as CardView)
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, i: Int) {
        val item = items[i]
        holder.onBind(
            item,
            activity.getNavigationBarHeight(),
            settings,
        )
    }

    override fun onViewRecycled(holder: SuggestionViewHolder) {
        val i = holder.bindingAdapterPosition
        if (i != -1)
            holder.recycle(items[i])
    }

    fun updateItems(items: List<LauncherItem>) {
        this.items = items
        notifyDataSetChanged()
    }
}