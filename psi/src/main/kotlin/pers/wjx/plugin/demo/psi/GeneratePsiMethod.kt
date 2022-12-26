package pers.wjx.plugin.demo.psi

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiClass
import pers.wjx.plugin.demo.psi.dialog.GeneratePsiMethodDialog
import javax.annotation.Nonnull

/**
 * 代码编辑器，右键 Action，鼠标位置新建一个方法
 * @author wjx
 */
class GeneratePsiMethod(@Nonnull psiClass: PsiClass, @Nonnull editor: Editor) :
    AnAction("Method", "Generate method", AllIcons.Nodes.Method) {

    var myPsiClass: PsiClass = psiClass
    var myEditor: Editor = editor

    override fun actionPerformed(e: AnActionEvent) {
        GeneratePsiMethodDialog(
            myPsiClass,
            myEditor.caretModel.currentCaret.offset
        ).show()
    }
}