package pers.wjx.plugin.demo.psi.dialog;

import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.refactoring.rename.RenameProcessor;
import com.intellij.refactoring.rename.RenameUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.wjx.plugin.demo.psi.common.NotificationUtils;
import pers.wjx.plugin.demo.psi.common.PsiBundle;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 创建/编辑 java 文件对话框
 *
 * @author wjx
 */
public class PsiJavaFileDialog extends DialogWrapper {
    private final boolean createState;
    private Project myProject;
    private PsiDirectory myPsiDirectory;
    private PsiJavaFile myPsiJavaFile;
    private JPanel contentPane;
    private JTextField className;
    private JComboBox<ClassType> types;
    private JRadioButton addAnn;
    private JTextField staticConstructor;
    private JPanel dataPane;

    public PsiJavaFileDialog(@Nonnull PsiDirectory directory) {
        super(true);
        createState = true;
        // 对话框标题
        setTitle(PsiBundle.INSTANCE.message("create.java.file"));
        myPsiDirectory = directory;
        myProject = myPsiDirectory.getProject();
        init();
    }

    public PsiJavaFileDialog(@Nonnull PsiJavaFile psiJavaFile) {
        super(true);
        createState = false;
        // 对话框标题
        setTitle(PsiBundle.INSTANCE.message("edit.java.file"));
        myPsiJavaFile = psiJavaFile;
        myProject = myPsiJavaFile.getProject();
        PsiClass aClass = myPsiJavaFile.getClasses()[0];
        // 回显
        className.setText(aClass.getName());
        if (aClass.isEnum()) {
            types.setSelectedItem(ClassType.Enum);
        } else if (aClass.isInterface()) {
            types.setSelectedItem(ClassType.Interface);
        } else {
            types.setSelectedItem(ClassType.Class);
            PsiAnnotation annotation = aClass.getAnnotation("lombok.Data");
            if (annotation != null) {
                String staticConstructorValue = AnnotationUtil.getStringAttributeValue(annotation, "staticConstructor");
                if (staticConstructorValue != null && !staticConstructorValue.isBlank()) {
                    this.staticConstructor.setText(staticConstructorValue);
                }
            } else {
                addAnn.setSelected(false);
            }
        }
        // 编辑态无法修改 java 类型
        types.setEnabled(false);
        init();
    }

    public String getClassName() {
        return className.getText().trim();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        for (ClassType type : ClassType.values()) {
            types.addItem(type);
        }
        // 如果是 Class 展示添加 @Data 注解的单选项
        types.addItemListener(l -> dataPane.setVisible(l.getItem().equals(ClassType.Class)));
        return contentPane;
    }

    @Override
    protected void doOKAction() {
        if (createState) {
            createJavaFile();
        } else {
            editJavaFile();
        }
        // 关闭窗口
        super.doOKAction();
    }

    /**
     * 创建 Java File
     */
    private void createJavaFile() {
        String className = getClassName();
        ClassType classType = (ClassType) types.getSelectedItem();
        // 对 Psi 执行非读操作(创建、编辑、删除)，需要用到 WriteCommandAction
        WriteCommandAction.runWriteCommandAction(myProject, PsiBundle.INSTANCE.message("create.java.file"), PsiBundle.INSTANCE.message("psi"), () -> {
            try {
                // 检查下是否可以在当前目录下创建指定类名的类
                JavaDirectoryService.getInstance().checkCreateClass(myPsiDirectory, className);
            } catch (Exception exception) {
                // 错误通知，右下角小弹框 (Balloon) UI :https://jetbrains.design/intellij/controls/balloon/
                // 可以新建个同类名看下效果
                NotificationUtils.INSTANCE.showWarning(exception.getMessage(), myProject);
                return;
            }
            // 根据类型创建类
            PsiClass aClass = createClass(className, Objects.requireNonNull(classType));
            // 编辑器定位到新建类
            aClass.navigate(true);
        });
    }

    /**
     * 编辑 Java file
     */
    private void editJavaFile() {
        String className = getClassName();
        PsiClass aClass = myPsiJavaFile.getClasses()[0];
        WriteCommandAction.runWriteCommandAction(myProject, PsiBundle.INSTANCE.message("edit.java.file"), PsiBundle.INSTANCE.message("psi"), () -> {
            // 修改类名
            if (!className.equals(myPsiJavaFile.getName())) {
                try {
                    RenameUtil.checkRename(myPsiJavaFile, className);
                } catch (Exception exception) {
                    // 错误通知，右下角小弹框 (Balloon) UI :https://jetbrains.design/intellij/controls/balloon/
                    NotificationUtils.INSTANCE.showWarning(exception.getMessage(), myProject);
                    return;
                }
            }
            // 修改注解
            if (!aClass.isEnum() && !aClass.isInterface()) {
                if (addAnn.isSelected()) {
                    generateDataAnn(aClass);
                } else {
                    PsiAnnotation annotation = aClass.getAnnotation("lombok.Data");
                    if (annotation != null) {
                        // 删除注解
                        annotation.delete();
                    }
                }
            }
        });
        // Refactorings should not be started inside write action, because they start progress inside and any read action from the progress task would cause the deadlock
        // 👀 RenameUtil.doRename();
        new RenameProcessor(myProject, aClass, className, false, false).run();

        // 编辑器定位到新建类
        myPsiJavaFile.navigate(true);
    }

    /**
     * 创建类也可以使用, PsiElementFactory.getInstance(project).createClassFromText()
     * {@link PsiJavaParserFacade#createClassFromText(java.lang.String, com.intellij.psi.PsiElement)}
     *
     * @param className 类名
     * @param type      类型
     * @return PsiClass
     */
    private PsiClass createClass(@Nonnull String className, @Nonnull ClassType type) {
        PsiClass aClass = null;
        switch (type) {
            case Class:
                aClass = JavaDirectoryService.getInstance().createClass(myPsiDirectory, className);
                // 添加 @Data 注解
                if (addAnn.isSelected()) {
                    generateDataAnn(aClass);
                }
                break;
            case Enum:
                aClass = JavaDirectoryService.getInstance().createEnum(myPsiDirectory, className);
                break;
            case Interface:
                aClass = JavaDirectoryService.getInstance().createInterface(myPsiDirectory, className);
                break;
        }
        return aClass;
    }

    /**
     * @param psiClass 注解所属类
     */
    private void generateDataAnn(@Nonnull PsiClass psiClass) {
        PsiAnnotation annotation = psiClass.getAnnotation("lombok.Data");

        if (annotation == null) {
            // 全类名注解 @lombok.Data
            annotation = Objects.requireNonNull(psiClass.getModifierList()).addAnnotation("lombok.Data");
        } else if (staticConstructor.getText().trim().isBlank()) {
            annotation = (PsiAnnotation) annotation.replace(PsiElementFactory.getInstance(myProject).createAnnotationFromText("@lombok.Data", psiClass));
        }
        if (!staticConstructor.getText().trim().isBlank()) {
            // 带参数的注解，@Data(staticConstructor = "of")，使用 PsiElementFactory 创建
            annotation = (PsiAnnotation) annotation.replace(PsiElementFactory.getInstance(myProject).createAnnotationFromText("@lombok.Data(staticConstructor = \"" + staticConstructor.getText().trim() + "\")", psiClass));
        }
        // 短类名引用, 自动 import lombok.Data; 并使用 @Data
        JavaCodeStyleManager.getInstance(myProject).shortenClassReferences(annotation);
    }

    /**
     * 校验
     */
    @Override
    protected @NotNull List<ValidationInfo> doValidateAll() {
        List<ValidationInfo> result = new ArrayList<>();
        if (className.getText().trim().isBlank()) {
            result.add(new ValidationInfo(PsiBundle.INSTANCE.message("class.name.requested")));
        }
        if (types.getSelectedItem() == null) {
            result.add(new ValidationInfo(PsiBundle.INSTANCE.message("class.type.requested")));
        }
        return result;
    }

    public enum ClassType {
        Class, Interface, Enum
    }
}
