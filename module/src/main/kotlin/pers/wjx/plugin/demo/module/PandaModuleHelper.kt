package pers.wjx.plugin.demo.module

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import freemarker.template.Template
import pers.wjx.plugin.demo.module.common.FreemarkerConfiguration
import java.io.StringWriter

/**
 * @author wjx
 */
class PandaModuleHelper(private var contentEntryPath: String) {
    private val freemarker: FreemarkerConfiguration = FreemarkerConfiguration("/templates")
    private val root = LocalFileSystem.getInstance().refreshAndFindFileByPath(contentEntryPath)

    fun savaImage(profile: PandaProfile) {
        if (profile.photo == null) {
            return
        }
        val copy = profile.photo!!.copy(this, root!!, "panda." + profile.photo!!.extension)
        profile.photo = copy
    }

    fun createPandaYmlByFreeMarker(
        project: Project?, profile: PandaProfile, templateName: String?, fileName: String?
    ) {
        try {
            var virtualFile = root!!.findChild(fileName!!)
            if (virtualFile == null) {
                virtualFile = root.createChildData(project, fileName)
            }
            val sw = StringWriter()
            val template: Template = freemarker.getTemplate(templateName)
            template.process(profile, sw)
            virtualFile.setBinaryContent(sw.toString().toByteArray())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}