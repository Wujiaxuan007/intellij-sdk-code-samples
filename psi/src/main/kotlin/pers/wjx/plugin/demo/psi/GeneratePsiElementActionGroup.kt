package pers.wjx.plugin.demo.psi

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.psi.PsiJavaFile
import pers.wjx.plugin.demo.psi.common.Icons

/**
 * 代码编辑器右键第一个行为分组，🐼
 * @author wjx
 */
class GeneratePsiElementActionGroup : DefaultActionGroup() {
    override fun update(e: AnActionEvent) {
        val presentation = e.presentation
        presentation.isVisible = false
        e.getData(CommonDataKeys.PROJECT) ?: return
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        psiFile ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        removeAll()
        presentation.isVisible = true;
        presentation.icon = Icons.PANDA

        if (psiFile is PsiJavaFile && psiFile.classes.isNotEmpty()) {
            add(GeneratePsiMethod(psiFile.classes[0], editor))
            add(GeneratePsiField(psiFile.classes[0], editor))
        }

        if (presentation.isVisible) {
            isPopup = true
        }
    }
}