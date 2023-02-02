package pers.wjx.plugin.demo.module

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import org.jetbrains.yaml.YAMLUtil
import org.jetbrains.yaml.psi.YAMLFile
import java.beans.PropertyChangeListener
import javax.swing.JComponent

/**
 * panda.yml 可视化页面
 *
 * @author wjx
 */
class PandaYmlEditor(private val pandaFile: YAMLFile) : UserDataHolderBase(), FileEditor {
    private var pandaWizardStep: PandaWizardStep? = null
    override fun dispose() {
        pandaWizardStep?.disposeUIResources()
    }

    override fun getComponent(): JComponent {
        var name = ""
        var age = 0
        var hobby = ""
        var sex = true
        var photoPath = ""
        YAMLUtil.getTopLevelKeys(pandaFile).forEach { kv ->
            when (kv.keyText) {
                "name" -> name = kv.valueText
                "age" -> age = kv.valueText.toInt()
                "hobby" -> hobby = kv.valueText
                "sex" -> sex = kv.valueText == "雌"
                "photo" -> photoPath = kv.valueText
            }
        }
        val imageFile = VirtualFileManager.getInstance().findFileByUrl("file://$photoPath")
        if (pandaWizardStep == null) {
            pandaWizardStep = PandaWizardStep(PandaProfile(name, age, sex, hobby, imageFile))
        } else {
            pandaWizardStep!!.setAge(age)
            pandaWizardStep!!.setSex(sex)
            pandaWizardStep!!.setHobby(hobby)
            pandaWizardStep!!.name = name
        }
        if (imageFile != null) {
            pandaWizardStep!!.previewImage(imageFile)
        }
        return pandaWizardStep!!.component
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return if (pandaWizardStep != null) {
            pandaWizardStep!!.component
        } else {
            component
        }
    }

    override fun getName(): String {
        return "Panda!"
    }

    override fun getFile(): VirtualFile? {
        return pandaFile.virtualFile
    }

    /**
     * 当定位到当前 Tab 页时，有可能源文件已经被修改过了，刷新 UI
     */
    override fun selectNotify() {
        pandaWizardStep?.component?.updateUI()
    }

    /**
     * 当取消选择当前 Tab 时，会执行这个方法
     *
     * 🐼 这里偷了个懒，讲道理如果可视化页面有改动也要反向更新源文件
     */
    override fun deselectNotify() {
        super.deselectNotify()
    }

    override fun setState(state: FileEditorState) {
    }

    override fun isModified(): Boolean {
        return false
    }

    override fun isValid(): Boolean {
        return true
    }

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {
    }

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {
    }

    override fun getCurrentLocation(): FileEditorLocation? {
        return null
    }
}