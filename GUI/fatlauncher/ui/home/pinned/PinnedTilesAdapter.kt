package net.tglt.android.fatlauncher.ui.home.pinned

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.posidon.android.computable.computedOrNull
import net.tglt.android.fatlauncher.LauncherContext
import net.tglt.android.fatlauncher.R
import net.tglt.android.fatlauncher.data.items.LauncherItem
import net.tglt.android.fatlauncher.data.items.getBanner
import net.tglt.android.fatlauncher.data.notification.NotificationData
import net.tglt.android.fatlauncher.providers.notification.NotificationService
import net.tglt.android.fatlauncher.ui.home.MainActivity
import net.tglt.android.fatlauncher.ui.home.pinned.TileDiffCallback.Companion.CHANGE_ALL
import net.tglt.android.fatlauncher.ui.home.pinned.TileDiffCallback.Companion.CHANGE_BANNER_TEXT
import net.tglt.android.fatlauncher.ui.home.pinned.TileDiffCallback.Companion.CHANGE_GRAPHICS
import net.tglt.android.fatlauncher.ui.home.pinned.TileDiffCallback.Companion.CHANGE_LABEL
import net.tglt.android.fatlauncher.ui.home.pinned.viewHolders.DropTargetViewHolder
import net.tglt.android.fatlauncher.ui.home.pinned.viewHolders.TileViewHolder
import net.tglt.android.fatlauncher.ui.home.pinned.viewHolders.bindDropTargetViewHolder

class PinnedTilesAdapter(
    val activity: MainActivity,
    val launcherContext: LauncherContext,
    val fragment: TileAreaFragment,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dropTargetIndex = -1
    private var items: MutableList<LauncherItem> = ArrayList()

    override fun getItemCount(): Int = items.size + if (dropTargetIndex == -1) 0 else 1

    val tileCount get() = items.size

    override fun getItemViewType(i: Int): Int {
        return when (i) {
            dropTargetIndex -> 1
            else -> 0
        }
    }

    fun adapterPositionToI(position: Int): Int {
        return when {
            dropTargetIndex == -1 -> position
            dropTargetIndex < position -> position - 1
            else -> position - 1
        }
    }

    fun iToAdapterPosition(i: Int): Int {
        return when {
            dropTargetIndex == -1 -> i
            dropTargetIndex < i -> i + 1
            else -> i
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> DropTargetViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.tile_drop_target, parent, false))
            else -> TileViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.tile, parent, false) as CardView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, ii: Int) {
        if (ii == dropTargetIndex) {
            holder as DropTargetViewHolder
            bindDropTargetViewHolder(holder)
            return
        }
        val item = items[adapterPositionToI(ii)]
        holder as TileViewHolder
        bindViewHolderUpdateAll(holder, item)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        ii: Int,
        payloads: MutableList<Any>
    ) {
        if (ii == dropTargetIndex) {
            holder as DropTargetViewHolder
            bindDropTargetViewHolder(holder)
            return
        }
        val item = items[adapterPositionToI(ii)]
        holder as TileViewHolder

        if (payloads.isEmpty()) {
            return bindViewHolderUpdateAll(holder, item)
        }
        payloads.forEach { payload ->
            payload as List<*>

            if (payload.contains(CHANGE_ALL))
                return bindViewHolderUpdateAll(holder, item)

            if (payload.contains(CHANGE_BANNER_TEXT))
                holder.updateBannerText(item.getBanner())

            if (payload.contains(CHANGE_LABEL))
                holder.updateLabel(item)

            if (payload.contains(CHANGE_GRAPHICS)) {
                val b = item.getBanner()
                holder.updateBackground(item, b.background.computedOrNull(), activity.settings, b)
            }
        }

        holder.updateTimeMark(item)

        println("payloads: " + payloads.joinToString("; ") { (it as List<*>?)?.joinToString().toString() })
    }

    private fun bindViewHolderUpdateAll(
        holder: TileViewHolder,
        item: LauncherItem
    ) {
        holder.bind(
            item,
            activity,
            activity.settings,
            onDragStart = {
                holder.itemView.isInvisible = true
            },
        )
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        val i = adapterPositionToI(holder.bindingAdapterPosition)
        if (i >= 0 && holder is TileViewHolder)
            holder.recycle(items[i])
    }

    fun updateItems(
        items: List<LauncherItem>
    ) {
        val c = TileDiffCallback(this.items, items, NotificationService.notifications, NotificationService.notifications)
        val diff = DiffUtil.calculateDiff(c)
        this.items = items.toMutableList()
        diff.dispatchUpdatesTo(this)
    }

    fun updateItems(old: List<NotificationData>, new: List<NotificationData>) {
        val c = TileDiffCallback(this.items, items, old, new)
        val diff = DiffUtil.calculateDiff(c)
        diff.dispatchUpdatesTo(this)
    }

    private fun updatePins(context: Context) {
        launcherContext.appManager.setPinned(context, ArrayList(items))
    }

    fun onDragOut(view: View, i: Int) {
        view.isVisible = true
        items.removeAt(i)
        dropTargetIndex = i
        notifyItemChanged(i)
        updatePins(view.context)
    }

    fun showDropTarget(i: Int) {
        if (i != dropTargetIndex) {
            when {
                i == -1 -> {
                    val old = dropTargetIndex
                    dropTargetIndex = -1
                    notifyItemRemoved(old)
                }
                dropTargetIndex == -1 -> {
                    dropTargetIndex = i
                    notifyItemInserted(i)
                }
                else -> {
                    val old = dropTargetIndex
                    dropTargetIndex = i
                    notifyItemMoved(old, i)
                }
            }
        }
    }

    fun onDrop(v: View, i: Int, clipData: ClipData) {
        if (i != dropTargetIndex) Toast.makeText(
            v.context,
            "PinnedTilesAdapter -> i = $i, dropTargetIndex = $dropTargetIndex",
            Toast.LENGTH_LONG
        ).show()
        val item = launcherContext.appManager.tryParseLauncherItem(clipData.getItemAt(0).text.toString(), v.context)
        item?.let { items.add(i, it) }
        dropTargetIndex = -1
        notifyItemChanged(i)
        updatePins(v.context)
    }
}