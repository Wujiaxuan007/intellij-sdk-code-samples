package pers.wjx.plugin.demo.psi.common

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

/**
 * https://plugins.jetbrains.com/docs/intellij/notifications.html#top-level-notifications-balloons
 * @author wjx
 */
object NotificationUtils {
    fun showWarning(content: String, project: Project) {
        Notification(PsiBundle.message("psi"), PsiBundle.message("psi"), content, NotificationType.WARNING)
            .notify(project)
    }
}