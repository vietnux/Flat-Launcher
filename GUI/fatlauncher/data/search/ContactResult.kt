package net.tglt.android.fatlauncher.data.search

import android.app.Activity
import android.content.Context
import android.view.View
import net.tglt.android.fatlauncher.data.items.ContactItem
import net.tglt.android.fatlauncher.ui.popup.appItem.ItemLongPress

class ContactResult private constructor(
    val contact: ContactItem,
) : CompactResult() {

    override val title get() = contact.label

    override val icon get() = contact.icon
    override val subtitle = null

    override var relevance = Relevance(0f)

    override val onLongPress = { v: View, _: Activity ->
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
        fun getList(context: Context): Collection<ContactResult> {
            return ContactItem.getList(context).map(::ContactResult)
        }
    }
}