package pers.wjx.plugin.demo.search

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import pers.wjx.plugin.demo.psi.common.Icons
import javax.swing.Icon

/**
 * @author wjx
 */
open class PandaFileType : LanguageFileType(Language.ANY) {
    companion object {
        val INSTANCE = PandaFileType()
    }

    override fun getName(): String {
        return "PANDA"
    }

    override fun getDisplayName(): String {
        return "Panda"
    }

    override fun getDescription(): String {
        return "Panda java file"
    }

    override fun getDefaultExtension(): String {
        return "panda"
    }

    override fun getIcon(): Icon {
        return Icons.PANDA
    }

}