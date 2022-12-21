package pers.wjx.plugin.demo.dialog;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import org.jetbrains.annotations.Nullable;
import pers.wjx.plugin.demo.common.NotificationUtils;
import pers.wjx.plugin.demo.common.PsiBundle;

import javax.swing.*;

public class CreatePsiJavaFileDialog extends DialogWrapper {
    private final PsiDirectory psiDirectory;
    private JPanel contentPane;
    private JTextField className;

    public String getClassName() {
        return className.getText().trim();
    }

    public CreatePsiJavaFileDialog(PsiDirectory directory) {
        super(true);
        psiDirectory = directory;
        // 对话框标题
        setTitle(PsiBundle.INSTANCE.message("create.java.file"));
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return contentPane;
    }

    @Override
    protected void doOKAction() {
        String className = getClassName();
        // 对 Psi 执行非读操作(创建、编辑、删除)，需要用到 WriteCommandAction
        WriteCommandAction.runWriteCommandAction(
                psiDirectory.getProject(),
                PsiBundle.INSTANCE.message("create.java.file"),
                PsiBundle.INSTANCE.message("psi"),
                () -> {
                    try {
                        // 检查下是否可以在当前目录下创建指定类名的类
                        JavaDirectoryService.getInstance().checkCreateClass(psiDirectory, className);
                    } catch (Exception exception) {
                        // 错误通知，右下角小弹框 (Balloon) UI :https://jetbrains.design/intellij/controls/balloon/
                        // 可以新建个同类名看下效果
                        NotificationUtils.INSTANCE.showWarning(exception.getMessage(), psiDirectory.getProject());
                    }
                    // 创建类
                    PsiClass aClass = JavaDirectoryService.getInstance().createClass(psiDirectory, className);
                    // 编辑器定位到新建类
                    aClass.navigate(true);
                }
        );
        // 关闭窗口
        super.doOKAction();
    }
}
