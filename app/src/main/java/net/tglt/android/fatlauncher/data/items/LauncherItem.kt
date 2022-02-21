package net.tglt.android.fatlauncher.data.items

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import io.posidon.android.computable.Computable
import net.tglt.android.fatlauncher.data.notification.NotificationData
import net.tglt.android.fatlauncher.providers.notification.NotificationService
import java.util.*

interface LauncherItem {

    val label: String

    val icon: Computable<Drawable>
    val color: Computable<Int>

    fun getBanner(notifications: List<NotificationData>): Banner

    /**
     * What to do when the item is clicked
     * [view] The view that was clicked
     */
    fun open(context: Context, view: View?)

    /**
     * Text representation of the item, used to save it
     */
    override fun toString(): String

    companion object {
        fun tryParse(
            string: String,
            appsByName: HashMap<String, MutableList<App>>,
            context: Context
        ): LauncherItem? = App.tryParse(string, appsByName)
            ?: ContactItem.tryParse(string, ContactItem.getList(context))
    }

    data class Banner(
        val title: String?,
        val text: String?,
        val background: Computable<Drawable?>,
        val bgOpacity: Float,
        val hideIcon: Boolean = false,
    ) {
        companion object {
            const val ALPHA_MULTIPLIER = .6f
        }
    }
}

fun LauncherItem.getBanner(): LauncherItem.Banner = getBanner(NotificationService.notifications)

fun LauncherItem.showProperties(view: View, backgroundColor: Int, textColor: Int) {
    if (this is App) {
        view.context.startActivity(Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .setData(Uri.parse("package:$packageName")))
    }
}