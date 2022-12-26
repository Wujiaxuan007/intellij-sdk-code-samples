package pers.wjx.plugin.demo.psi

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiDirectory
import pers.wjx.plugin.demo.psi.common.Icons
import pers.wjx.plugin.demo.psi.dialog.PsiJavaFileDialog

/**
 * 创建一个 java 类
 * @author wjx
 */
class CreatePsiJavaFile : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        // 从 AnActionEvent 上下文获取 PSI 元素
        val psiElement = e.getData(CommonDataKeys.PSI_ELEMENT)
        val psiDirectory = if (psiElement is PsiDirectory) {
            psiElement
        } else {
            psiElement!!.containingFile.containingDirectory
        }
        // 弹出对话框
        PsiJavaFileDialog(psiDirectory).show()
    }

    override fun update(e: AnActionEvent) {
        val presentation = e.presentation
        // 🐼 设置动作图标
        presentation.icon = Icons.PANDA
        // 动作不可见
        presentation.isVisible = false
        e.getData(CommonDataKeys.PROJECT) ?: return
        //动作可见
        presentation.isVisible = true
    }
}