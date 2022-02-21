package net.tglt.android.fatlauncher.ui.popup.listPopup

import android.graphics.drawable.Drawable
import android.view.View

class ListPopupItem(
    val text: String,
    val description: String? = null,
    val icon: Drawable? = null,
    val isTitle: Boolean = false,
    val value: Any? = null,
    val states: Int = 0,
    val unsafeLevel: Int = -1,
    val onStateChange: ((View, Int) -> Unit)? = null,
    val onClick: ((View) -> Unit)? = null,
)