package pers.wjx.plugin.demo.module

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.sun.istack.NotNull
import org.jetbrains.yaml.psi.YAMLFile
import pers.wjx.plugin.demo.module.common.ModuleBundle

/**
 * @author wjx
 */
class PandaYmlFileProvider : FileEditorProvider, DumbAware {
    override fun accept(project: Project, @NotNull file: VirtualFile): Boolean {
        return file.name == "panda.yml"
    }

    override fun createEditor(project: Project, @NotNull file: VirtualFile): FileEditor {
        val psiFile = PsiManager.getInstance(project).findFile(file)
        return PandaYmlEditor(psiFile as YAMLFile)
    }

    override fun getEditorTypeId(): String {
        return ModuleBundle.message("panda")
    }

    override fun getPolicy(): FileEditorPolicy {
        return FileEditorPolicy.PLACE_BEFORE_DEFAULT_EDITOR
    }
}