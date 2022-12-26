package pers.wjx.plugin.demo.psi.dialog;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;
import pers.wjx.plugin.demo.psi.common.PsiBundle;
import pers.wjx.plugin.demo.psi.common.PsiBundle;

import javax.swing.*;

/**
 * 删除文件对话框
 *
 * @author wjx
 */
public class DeleteFileDialog extends DialogWrapper {
    private final PsiFile myFile;
    private JPanel contentPane;
    private JLabel deleteTip;

    public DeleteFileDialog(PsiFile file) {
        super(true);
        myFile = file;
        // 对话框标题
        setTitle(PsiBundle.INSTANCE.message("delete.file"));
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        deleteTip.setText("Delete file \"" + myFile.getName() + "\"?");
        return contentPane;
    }

    @Override
    protected void doOKAction() {
        WriteCommandAction.runWriteCommandAction(
                myFile.getProject(),
                PsiBundle.INSTANCE.message("delete.file"),
                PsiBundle.INSTANCE.message("psi"),
                myFile::delete);
        super.doOKAction();
    }
}
