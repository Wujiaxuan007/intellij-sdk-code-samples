package pers.wjx.plugin.demo.psi.common

import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.util.function.Consumer
import javax.swing.*

/**
 * @author wjx
 */
fun initPsiClassFQCNComboBox(project: Project, jComboBox: JComboBox<String>, addVoid: Boolean? = false) {
    // 加几个基础类型选项
    if (addVoid == true) {
        jComboBox.addItem(Void::class.javaPrimitiveType!!.name)
    }
    jComboBox.addItem(Int::class.javaPrimitiveType!!.name)
    jComboBox.addItem(Long::class.javaPrimitiveType!!.name)
    jComboBox.addItem(Long::class.java.name)
    jComboBox.addItem(String::class.java.name)
    jComboBox.addItem(Int::class.java.name)
    jComboBox.addItem(Any::class.java.name)

    // 查找当前项目所有自定义的 Java 类
    FileTypeIndex.getFiles(JavaFileType.INSTANCE, GlobalSearchScope.projectScope(project))
        .forEach(Consumer forEach@{ virtualFile: VirtualFile? ->
            val psiFile = PsiManager.getInstance(project).findFile(virtualFile!!) as? PsiJavaFile ?: return@forEach
            for (aClass in psiFile.classes) {
                jComboBox.addItem(aClass.qualifiedName)
            }
        })
    // 选择器渲染成短类名
    jComboBox.setRenderer { list: JList<out String?>?, value: String?, index: Int, isSelected: Boolean, cellHasFocus: Boolean ->
        val strings = value!!.split("\\.").toTypedArray()
        DefaultListCellRenderer().getListCellRendererComponent(
            list, strings[strings.size - 1], index, isSelected, cellHasFocus
        )
    }
    // 支持搜索功能
    decorateSearchSelector(jComboBox)
}

/**
 * 带搜索功能的选择器
 */
fun decorateSearchSelector(jcb: JComboBox<String>) {
    jcb.isEditable = true
    val textField = jcb.editor.editorComponent as JTextField

    val defaultsItems: HashSet<String> = HashSet()
    for (i in 0 until jcb.itemCount) {
        defaultsItems.add(jcb.getItemAt(i))
    }
    textField.addKeyListener(object : KeyAdapter() {
        var metaOrCtrl = false
        override fun keyReleased(e: KeyEvent) {
            val code = e.keyCode
            if (code == KeyEvent.VK_BACK_SPACE || code == KeyEvent.VK_V && metaOrCtrl) {
                invokeLater()
            }
            if (code == KeyEvent.VK_ENTER) {
                if (jcb.selectedItem == textField.text) {
                    jcb.hidePopup()
                } else {
                    invokeLater()
                }
            }
            if (e.isMetaDown || e.isControlDown) {
                metaOrCtrl = false
            }
        }

        override fun keyPressed(e: KeyEvent) {
            val code = e.keyCode
            if (code == KeyEvent.VK_ENTER) {
                val item = jcb.selectedItem
                if (item != null) {
                    textField.text = item.toString()
                }
                return
            }
            if (e.isMetaDown || e.isControlDown) {
                metaOrCtrl = true
            }
            if (KeyEvent.VK_LEFT <= code && code <= KeyEvent.VK_DOWN
                || e.keyChar == KeyEvent.CHAR_UNDEFINED
                || e.modifiersEx != 0 && !e.isShiftDown
            ) {
                return
            }
            invokeLater()
        }

        private fun invokeLater() {
            SwingUtilities.invokeLater {
                val text = textField.text
                val filters: ArrayList<String> = ArrayList()
                for (item in defaultsItems) {
                    if (item.lowercase().contains(text.lowercase())) {
                        filters.add(item)
                    }
                }
                jcb.removeAllItems()
                if (filters.isNotEmpty()) {
                    for (str in filters) {
                        jcb.addItem(str)
                    }
                }
                if (jcb.isPopupVisible) {
                    jcb.hidePopup()
                }
                jcb.showPopup()
                textField.text = text
            }
        }
    })
}