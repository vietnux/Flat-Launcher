package net.tglt.android.fatlauncher.ui.home.main.tile

import androidx.recyclerview.widget.DiffUtil
import net.tglt.android.fatlauncher.data.items.LauncherItem

class TileDiffCallback(
    private val old: List<LauncherItem>,
    private val new: List<LauncherItem>,
) : DiffUtil.Callback() {

    override fun getOldListSize() = old.size
    override fun getNewListSize() = new.size

    override fun areItemsTheSame(oldI: Int, newI: Int): Boolean {
        val old = old[oldI]
        val new = new[newI]
        return old == new
    }

    override fun areContentsTheSame(oldI: Int, newI: Int): Boolean {
        val old = old[oldI]
        val new = new[newI]
        return old.label == new.label
    }
}