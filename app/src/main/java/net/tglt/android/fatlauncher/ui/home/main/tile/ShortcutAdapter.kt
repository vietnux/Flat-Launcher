package net.tglt.android.fatlauncher.ui.home.main.tile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.tglt.android.fatlauncher.R
import net.tglt.android.fatlauncher.data.items.LauncherItem
import net.tglt.android.fatlauncher.providers.item.GraphicsLoader
import net.tglt.android.fatlauncher.ui.home.main.tile.viewHolders.ShortcutViewHolder
import net.tglt.android.fatlauncher.util.storage.Settings

class ShortcutAdapter(
    private val shortcuts: List<LauncherItem>,
    val graphicsLoader: GraphicsLoader,
    val settings: Settings,
    val onLongPress: () -> Unit,
) : RecyclerView.Adapter<ShortcutViewHolder>() {

    override fun getItemCount(): Int = shortcuts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortcutViewHolder {
        return ShortcutViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.tile_shortcut, parent, false)).apply {
            itemView.setOnLongClickListener {
                onLongPress()
                true
            }
        }
    }

    override fun onBindViewHolder(holder: ShortcutViewHolder, i: Int) {
        val s = shortcuts[i]
        holder.onBind(s, graphicsLoader, settings)
    }
}
