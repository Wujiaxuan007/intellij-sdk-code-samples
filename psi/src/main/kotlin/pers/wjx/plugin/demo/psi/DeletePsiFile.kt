package pers.wjx.plugin.demo.psi

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import pers.wjx.plugin.demo.psi.common.Icons
import pers.wjx.plugin.demo.psi.dialog.DeleteFileDialog

/**
 * 删除文件
 * @author wjx
 */
class DeletePsiFile : AnAction("Delete...", "Delete file", Icons.PANDA) {
    override fun actionPerformed(e: AnActionEvent) {
        DeleteFileDialog(e.getData(CommonDataKeys.PSI_FILE)).show()
    }

    override fun update(e: AnActionEvent) {
        val presentation = e.presentation
        // 动作不可见
        presentation.isVisible = false
        e.getData(CommonDataKeys.PSI_FILE) ?: return
        //动作可见
        presentation.isVisible = true
    }
}