package pers.wjx.plugin.demo.psi

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiJavaFile
import pers.wjx.plugin.demo.psi.common.Icons
import pers.wjx.plugin.demo.psi.dialog.PsiJavaFileDialog

/**
 * 编辑 java 文件: 类名、注解
 * @author wjx
 */
class EditPsiJavaFile : AnAction("Edit Java File", "Edit java file", Icons.PANDA) {
    override fun actionPerformed(e: AnActionEvent) {
        val psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (psiFile is PsiJavaFile) {
            PsiJavaFileDialog(psiFile).show()
        }
    }

    override fun update(e: AnActionEvent) {
        val presentation = e.presentation
        // 动作不可见
        presentation.isVisible = false
        e.getData(CommonDataKeys.PROJECT) ?: return
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        psiFile ?: return
        if (psiFile !is PsiJavaFile) return
        //动作可见
        presentation.isVisible = true
    }
}