package net.tglt.android.fatlauncher.ui.home.pinned

import androidx.recyclerview.widget.DiffUtil
import net.tglt.android.fatlauncher.data.items.LauncherItem
import net.tglt.android.fatlauncher.data.notification.NotificationData
import java.util.*

class TileDiffCallback(
    val old: List<LauncherItem>,
    val new: List<LauncherItem>,
    val oldNotifications: List<NotificationData>,
    val newNotifications: List<NotificationData>,
) : DiffUtil.Callback() {

    fun getOld(i: Int) = old[i]
    fun getNew(i: Int) = new[i]

    override fun getOldListSize() = old.size
    override fun getNewListSize() = new.size

    override fun areItemsTheSame(oldI: Int, newI: Int): Boolean {
        val old = getOld(oldI)
        val new = getNew(newI)
        return old == new
    }

    override fun areContentsTheSame(oldI: Int, newI: Int): Boolean {
        val old = getOld(oldI)
        val new = getNew(newI)
        val oldBanner = old.getBanner(oldNotifications)
        val newBanner = new.getBanner(newNotifications)
        return old.label == new.label
            && old.icon.isComputed()
            && new.icon.isComputed()
            && old.icon.computed() === new.icon.computed()
            && old.color.isComputed()
            && new.color.isComputed()
            && old.color.computed() == new.color.computed()
            && oldBanner.background.isComputed()
            && newBanner.background.isComputed()
            && oldBanner.background.computed() === newBanner.background.computed()
            && oldBanner == newBanner
    }

    override fun getChangePayload(oldI: Int, newI: Int): Any {
        val old = getOld(oldI)
        val new = getNew(newI)
        val oldBanner = old.getBanner(oldNotifications)
        val newBanner = new.getBanner(newNotifications)
        val changes = LinkedList<Int>()
        if (
            !old.icon.isComputed() ||
            !new.icon.isComputed() ||
            !old.color.isComputed() ||
            !new.color.isComputed() ||
            !oldBanner.background.isComputed() ||
            !newBanner.background.isComputed() ||
            oldBanner.hideIcon != newBanner.hideIcon ||
            oldBanner.bgOpacity != newBanner.bgOpacity
        ) {
            changes += CHANGE_ALL
            return changes
        }
        if (
            oldBanner.title != newBanner.title ||
            oldBanner.text != newBanner.text
        ) changes += CHANGE_BANNER_TEXT
        if (old.label != new.label)
            changes += CHANGE_LABEL
        if (
            old.color.computed() != new.color.computed() ||
            old.icon.computed() !== new.icon.computed() ||
            oldBanner.background.computed() !== newBanner.background.computed()
        ) changes += CHANGE_GRAPHICS
        return changes
    }

    companion object {
        const val CHANGE_ALL = -1
        const val CHANGE_BANNER_TEXT = 0
        const val CHANGE_LABEL = 1
        const val CHANGE_GRAPHICS = 2
    }
}