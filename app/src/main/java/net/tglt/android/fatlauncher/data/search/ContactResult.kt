package net.tglt.android.fatlauncher.data.search

import android.app.Activity
import android.content.Context
import android.view.View
import net.tglt.android.fatlauncher.data.items.ContactItem
import net.tglt.android.fatlauncher.providers.item.ContactLoader
import net.tglt.android.fatlauncher.providers.item.GraphicsLoader
import net.tglt.android.fatlauncher.ui.popup.appItem.ItemLongPress

class ContactResult private constructor(
    val contact: ContactItem,
) : CompactResult() {

    override val launcherItem get() = contact

    override val title get() = contact.label

    override val subtitle = null

    override var relevance = Relevance(0f)

    override val onLongPress = { _: GraphicsLoader, v: View, _: Activity ->
        ItemLongPress.onItemLongPress(
            v,
            contact,
        )
        true
    }

    override fun open(view: View) {
        contact.open(view.context, view)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContactResult

        if (contact == other.contact) return true

        return true
    }

    override fun hashCode() = contact.hashCode()
    override fun toString() = contact.toString()

    companion object {
        fun getList(context: Context): List<ContactResult> =
            ContactLoader.load(context).map(::ContactResult)
    }
}