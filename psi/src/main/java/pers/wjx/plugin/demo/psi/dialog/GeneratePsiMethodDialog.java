package pers.wjx.plugin.demo.psi.dialog;

import com.intellij.codeInsight.actions.OptimizeImportsProcessor;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.codeInsight.generation.GenerateMembersUtil;
import com.intellij.codeInsight.generation.PsiGenerationInfo;
import com.intellij.openapi.actionSystem.ActionToolbarPosition;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.ui.JBColor;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.AbstractTableCellEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.wjx.plugin.demo.psi.common.PsiBundle;
import pers.wjx.plugin.demo.psi.common.ComponentUtilsKt;
import pers.wjx.plugin.demo.psi.table.MethodInputTableModel;
import pers.wjx.plugin.demo.psi.table.MethodInputTableModel.Parameter;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 创建 java 方法对话框
 *
 * @author wjx
 */
public class GeneratePsiMethodDialog extends DialogWrapper {
    private final Project myProject;
    private final PsiClass myPsiClass;
    private final int myOffSet;
    private final List<Parameter> inputs = new ArrayList<>();
    private JPanel contentPane;
    private JTextField methodName;
    // .form 文件中 inputPane 使用了 CardLayout
    private JPanel inputPane;
    private JPanel outputPane;
    private JComboBox<String> outputTypeSelector;
    protected JComboBox<String> inputTypeSelector = new ComboBox<>();

    public GeneratePsiMethodDialog(PsiClass psiClass, int offSet) {
        super(true);
        myProject = psiClass.getProject();
        myPsiClass = psiClass;
        myOffSet = offSet;
        setTitle(PsiBundle.INSTANCE.message("generate.java.method"));
        init();
    }

    public List<Parameter> getInputs() {
        return inputs;
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        ComponentUtilsKt.initPsiClassFQCNComboBox(myProject, inputTypeSelector, false);
        ComponentUtilsKt.initPsiClassFQCNComboBox(myProject, outputTypeSelector, true);
        inputPane.add(createInputsTable());
        return contentPane;
    }

    @Override
    protected void doOKAction() {
        PsiElementFactory factory = PsiElementFactory.getInstance(myProject);
        StringBuilder sb = new StringBuilder();
        sb.append("public ").append(outputTypeSelector.getSelectedItem()).append(" ").append(getMethodName())
                .append("( ");
        for (Parameter input : inputs) {
            if (!input.getType().trim().isBlank() && !input.getName().trim().isBlank()) {
                sb.append(input.getType()).append(" ").append(input.getName()).append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("){\n}");
        // 根据文本创建方法
        var psiMethod = factory.createMethodFromText(sb.toString(), null);
        PsiGenerationInfo<PsiMethod> ItemInfo = new PsiGenerationInfo<>(psiMethod);

        WriteCommandAction.runWriteCommandAction(myProject,
                PsiBundle.INSTANCE.message("generate.java.method"),
                PsiBundle.INSTANCE.message("psi"), () -> {
                    // 指定位置插入方法
                    GenerateMembersUtil.insertMembersAtOffset(myPsiClass, myOffSet, Collections.singletonList(ItemInfo));
                    // 格式化
                    new ReformatCodeProcessor(myPsiClass.getContainingFile(), false).run();
                    // 优化 import
                    new OptimizeImportsProcessor(myProject, myPsiClass.getContainingFile()).run();
                });
        super.doOKAction();
    }

    /**
     * 校验
     */
    @Override
    protected @NotNull List<ValidationInfo> doValidateAll() {
        List<ValidationInfo> result = new ArrayList<>();
        if (methodName.getText().trim().isBlank()) {
            result.add(new ValidationInfo(PsiBundle.INSTANCE.message("method.name.requested")));
        }
        return result;
    }


    private String getMethodName() {
        return methodName.getText().trim();
    }

    /**
     * @return 入参的表格
     */
    private JBScrollPane createInputsTable() {
        MethodInputTableModel inputTableModel = new MethodInputTableModel(this);
        JBTable inputsTable = new JBTable(inputTableModel);
        JTableHeader tableHeader = inputsTable.getTableHeader();
        tableHeader.setBackground(JBColor.LIGHT_GRAY);
        inputsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inputsTable.putClientProperty("terminateEditOnFocusLost", true);
        inputsTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        inputsTable.getEmptyText().setText("No inputs");
        inputsTable.setDefaultEditor(String.class, new MyInputsEditorComponent(inputTypeSelector));

        JPanel tablePanel = createToolbarDecorator(inputsTable, inputTableModel, this);
        TableColumnModel columnModel = inputsTable.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(150);
        }
        return new JBScrollPane(tablePanel);
    }

    // 入参表格 Toolbar: + - 操作
    private JPanel createToolbarDecorator(JBTable myTable, MethodInputTableModel inputTableModel, GeneratePsiMethodDialog dialog) {
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(myTable).setAddAction(button -> {
            stopEditing(myTable);
            int index = inputTableModel.getRowCount();
            Parameter input = new Parameter("", "");
            inputTableModel.addRow(index, input);
            myTable.changeSelection(index, 0, false, false);
            myTable.editCellAt(index, 0);
            dialog.getInputs().set(index, input);
            myTable.getSelectionModel().setSelectionInterval(index, index);
            inputTableModel.fireTableRowsInserted(index, index);
        }).setRemoveAction(button -> {
            stopEditing(myTable);
            int selectedRow = myTable.getSelectedRow();
            inputTableModel.removeRow(selectedRow);
            if (myTable.getRowCount() >= selectedRow && selectedRow > 0) {
                myTable.changeSelection(selectedRow - 1, 1, false, false);
                myTable.editCellAt(selectedRow - 1, 1);
            }
        }).setToolbarPosition(ActionToolbarPosition.TOP).disableUpDownActions();
        return toolbarDecorator.createPanel();
    }

    private static void stopEditing(JBTable myTable) {
        if (myTable.isEditing()) {
            TableCellEditor editor = myTable.getCellEditor();
            if (editor != null) {
                editor.stopCellEditing();
            }
        }
    }

    public static class MyInputsEditorComponent extends AbstractTableCellEditor {
        private final JComboBox<String> jComboBox;

        public MyInputsEditorComponent(JComboBox<String> jComboBox) {
            this.jComboBox = jComboBox;
        }

        public JComboBox<String> getJComboBox() {
            return jComboBox;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return jComboBox;
        }

        @Override
        public Object getCellEditorValue() {
            return this;
        }
    }
}
