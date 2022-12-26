package pers.wjx.plugin.demo.psi.table

import com.intellij.util.ui.ItemRemovable
import org.apache.commons.lang3.StringUtils
import pers.wjx.plugin.demo.psi.dialog.GeneratePsiMethodDialog
import javax.swing.table.AbstractTableModel

/**
 * @author wjx
 */
class MethodInputTableModel(dialog: GeneratePsiMethodDialog) : AbstractTableModel(), ItemRemovable {
    private val myDialog: GeneratePsiMethodDialog = dialog

    private val ourColumnNames = arrayOf(
        "参数类型", "参数名"
    )

    // 参数类型用了 String
    // 参数名用了 Object 而不是 String 是有意为之
    // inputsTable.setDefaultEditor(String.class, new MyInputsEditorComponent(inputTypeSelector));
    // 因为 setDefaultEditor 是根据 classType 来渲染的
    private val ourColumnClasses = arrayOf(
        String::class.java, Object::class.java
    )

    override fun getColumnName(column: Int): String {
        return ourColumnNames[column]
    }

    override fun getColumnClass(column: Int): Class<*> {
        return ourColumnClasses[column]
    }

    override fun getColumnCount(): Int {
        return ourColumnNames.size
    }

    override fun getRowCount(): Int {
        return myDialog.inputs.size
    }

    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
        return true
    }

    override fun getValueAt(row: Int, column: Int): Any {
        val input: Parameter = myDialog.inputs[row]
        return when (column) {
            0 -> {
                input.type.split(".").last()
            }
            1 -> {
                input.name
            }
            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    override fun setValueAt(value: Any, row: Int, column: Int) {
        val input: Parameter = myDialog.inputs[row]
        when (column) {
            0 -> {
                if (value is GeneratePsiMethodDialog.MyInputsEditorComponent) {
                    input.type = value.jComboBox.selectedItem!!.toString()
                    input.name = "my" + StringUtils.capitalize(input.type.split(".").last())
                    setValueAt(input.name, row, 1)
                    fireTableCellUpdated(row, 1)
                }
            }
            1 -> input.name = value.toString()
            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    override fun removeRow(index: Int) {
        myDialog.inputs.removeAt(index)
        fireTableRowsDeleted(index, index)
    }

    fun addRow(index: Int, input: Parameter?) {
        myDialog.inputs.add(input)
        fireTableRowsInserted(index, index)
    }

    class Parameter(var type: String, var name: String)
}
