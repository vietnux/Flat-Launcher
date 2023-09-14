package net.tglt.android.fatlauncher.providers.notification

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import net.tglt.android.fatlauncher.data.notification.NotificationData
import net.tglt.android.fatlauncher.data.notification.NotificationGroupData
import net.tglt.android.fatlauncher.data.notification.TempNotificationData

object NotificationCreator {

    private inline fun getSource(context: Context, n: StatusBarNotification): String {
        return context.packageManager.getApplicationLabel(context.packageManager.getApplicationInfo(n.packageName, 0)).toString()
    }

    private inline fun getTitle(extras: Bundle): CharSequence? {
        val title = extras.getCharSequence(Notification.EXTRA_TITLE)
        if (title == null || title.toString().replace(" ", "").isEmpty()) {
            return null
        }
        return title
    }

    private inline fun getText(extras: Bundle): CharSequence? {
        val messages = extras.getParcelableArray(Notification.EXTRA_MESSAGES)
        return if (messages == null) {
            extras.getCharSequence(Notification.EXTRA_BIG_TEXT)
                ?: extras.getCharSequence(Notification.EXTRA_TEXT)
        } else buildString {
            messages.forEach {
                val bundle = it as Bundle
                appendLine(bundle.getCharSequence("text"))
            }
            delete(lastIndex, length)
        }
    }

    private inline fun getSmallIcon(context: Context, n: StatusBarNotification): Drawable? {
        return n.notification.smallIcon?.loadDrawable(context)
    }

    private inline fun getLargeIcon(context: Context, n: StatusBarNotification): Drawable? {
        return n.notification.getLargeIcon()?.loadDrawable(context)
    }

    private inline fun getBigImage(context: Context, extras: Bundle): Drawable? {
        val b = extras[Notification.EXTRA_PICTURE] as Bitmap?
        if (b != null) {
            try {
                if (b.width < 64 || b.height < 64) {
                    return null
                }
                return BitmapDrawable(context.resources, b)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    private inline fun getBigImageFromMessages(context: Context, messagingStyle: NotificationCompat.MessagingStyle): Drawable? {
        messagingStyle.messages.asReversed().forEach {
            it.dataUri?.let { uri ->
                runCatching {
                    return Drawable.createFromStream(context.contentResolver.openInputStream(uri), null)
                }
            }
        }
        messagingStyle.historicMessages.asReversed().forEach {
            it.dataUri?.let { uri ->
                runCatching {
                    return Drawable.createFromStream(context.contentResolver.openInputStream(uri), null)
                }
            }
        }
        return null
    }

    inline fun getImportance(importance: Int): Int {
        return when (importance) {
            NotificationManager.IMPORTANCE_NONE,
            NotificationManager.IMPORTANCE_MIN -> -1
            NotificationManager.IMPORTANCE_LOW,
            NotificationManager.IMPORTANCE_DEFAULT -> 0
            NotificationManager.IMPORTANCE_HIGH -> 1
            NotificationManager.IMPORTANCE_MAX -> 2
            else -> throw IllegalStateException("Invalid notification importance")
        }
    }

    fun create(context: Context, notification: StatusBarNotification, service: NotificationService): TempNotificationData {

        val extras = notification.notification.extras

        var title = getTitle(extras)
        var text = getText(extras)
        if (title == null) {
            title = text
            text = null
        }

        val source = getSource(context, notification)
        val icon = getSmallIcon(context, notification)!!
        val color = notification.notification.color

        val channel = NotificationManagerCompat.from(context).getNotificationChannel(notification.notification.channelId)
        val importance = channel?.importance?.let { getImportance(it) } ?: 0

        var bigPic = getBigImage(context, extras)

        val messagingStyle = NotificationCompat.MessagingStyle.extractMessagingStyleFromNotification(
            notification.notification)
        val isConversation = messagingStyle != null
        if (messagingStyle != null) {
            messagingStyle.conversationTitle?.toString()?.let { title = it }
            messagingStyle.messages.lastOrNull()?.text?.toString()?.let { text = it }
            if (bigPic == null) {
                bigPic = getBigImageFromMessages(context, messagingStyle)
            }
        }

        val key = notification.key
        val autoCancel = notification.notification.flags and Notification.FLAG_AUTO_CANCEL != 0

        return TempNotificationData(
            group = NotificationGroupData(
                source = source,
                title = title?.toString() ?: "",
                sourcePackageName = notification.packageName,
                notifications = listOf(NotificationData(
                    icon = icon,
                    description = text?.toString(),
                    image = bigPic,
                    color = color,
                    open = {
                        try {
                            notification.notification.contentIntent?.send()
                            if (autoCancel)
                                service.cancelNotification(key)
                        }
                        catch (e: Exception) {
                            service.cancelNotification(key)
                            e.printStackTrace()
                        }
                    },
                    cancel = {
                        service.cancelNotification(key)
                    }
                ),
            )),
            millis = notification.postTime,
            importance = importance.coerceAtLeast(0),
            isConversation = isConversation,
        )
    }
}