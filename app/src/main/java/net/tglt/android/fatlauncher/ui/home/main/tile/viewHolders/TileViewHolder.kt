package net.tglt.android.fatlauncher.ui.home.main.tile.viewHolders

import android.app.Activity
import android.view.View
import androidx.core.view.isVisible
import net.tglt.android.fatlauncher.data.items.LauncherItem
import net.tglt.android.fatlauncher.providers.item.GraphicsLoader
import net.tglt.android.fatlauncher.util.storage.Settings
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

interface TileViewHolder {

    fun bind(
        item: LauncherItem,
        activity: Activity,
        settings: Settings,
        graphicsLoader: GraphicsLoader,
        onDragStart: (View) -> Unit,
    )

    fun recycle()
}

@OptIn(ExperimentalContracts::class)
inline fun <T: View, R> T.hideIfNullOr(value: R?, block: T.(R) -> Unit) {
    contract { callsInPlace(block, InvocationKind.AT_MOST_ONCE) }
    if (value == null) {
        isVisible = false
    } else {
        isVisible = true
        block(value)
    }
}