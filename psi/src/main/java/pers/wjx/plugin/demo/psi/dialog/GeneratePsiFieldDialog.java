package pers.wjx.plugin.demo.psi.dialog;

import com.intellij.codeInsight.actions.OptimizeImportsProcessor;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.codeInsight.generation.GenerateMembersUtil;
import com.intellij.codeInsight.generation.PsiGenerationInfo;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.wjx.plugin.demo.psi.common.PsiBundle;
import pers.wjx.plugin.demo.psi.common.ComponentUtilsKt;
import pers.wjx.plugin.demo.psi.common.PsiBundle;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 创建 java 字段
 *
 * @author wjx
 */
public class GeneratePsiFieldDialog extends DialogWrapper {
    private final Project myProject;
    private final PsiClass myPsiClass;
    private final int myOffSet;
    private JPanel contentPane;
    private JComboBox<String> type;
    private JTextField fieldName;

    public GeneratePsiFieldDialog(@Nonnull PsiClass psiClass, int offSet) {
        super(true);
        myProject = psiClass.getProject();
        myPsiClass = psiClass;
        myOffSet = offSet;
        setTitle(PsiBundle.INSTANCE.message("generate.java.field"));
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        ComponentUtilsKt.initPsiClassFQCNComboBox(myPsiClass.getProject(), type, false);
        return contentPane;
    }

    @Override
    protected void doOKAction() {
        PsiElementFactory factory = PsiElementFactory.getInstance(myProject);
        // 创建 field, 也可使用 factory.createFieldFromText
        var psiType = PsiElementFactory.getInstance(myProject)
                .createTypeFromText(Objects.requireNonNull(type.getSelectedItem()).toString(), null);
        var psiField = factory.createField(StringUtils.uncapitalize(fieldName.getText().trim()), psiType);
        PsiGenerationInfo<PsiField> psi = new PsiGenerationInfo<>(psiField);
        WriteCommandAction.runWriteCommandAction(myProject,
                PsiBundle.INSTANCE.message("generate.java.field"),
                PsiBundle.INSTANCE.message("psi"), () -> {
                    // 指定位置插入方法
                    GenerateMembersUtil.insertMembersAtOffset(myPsiClass, myOffSet, Collections.singletonList(psi));
                    // 格式化
                    new ReformatCodeProcessor(myPsiClass.getContainingFile(), false).run();
                    // 优化 import
                    new OptimizeImportsProcessor(myProject, myPsiClass.getContainingFile()).run();
                });
        super.doOKAction();
    }

    @Override
    protected @NotNull List<ValidationInfo> doValidateAll() {
        List<ValidationInfo> result = new ArrayList<>();
        if (fieldName.getText().trim().isBlank()) {
            result.add(new ValidationInfo(PsiBundle.INSTANCE.message("field.name.requested")));
        }
        return result;
    }
}
