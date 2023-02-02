package pers.wjx.plugin.demo.module.common

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupManager
import com.intellij.util.DisposeAwareRunnable

/**
 * @author wjx
 */
object InvokeUtil {
    fun runWhenInitialized(project: Project, r: Runnable) {
        if (project.isDisposed) {
            return
        }
        if (isNoBackgroundMode()) {
            r.run()
            return
        }
        if (!project.isInitialized) {
            try {
                StartupManager.getInstance(project).registerPostStartupActivity(DisposeAwareRunnable.create(r, project))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return
        }
        runDumbAware(project, r)
    }

    private fun isNoBackgroundMode(): Boolean {
        return ApplicationManager.getApplication().isUnitTestMode
                || ApplicationManager.getApplication().isHeadlessEnvironment
    }

    private fun runDumbAware(project: Project?, r: Runnable) {
        if (DumbService.isDumbAware(r)) {
            r.run()
        } else {
            DumbService.getInstance(project!!).runWhenSmart(DisposeAwareRunnable.create(r, project))
        }
    }
}