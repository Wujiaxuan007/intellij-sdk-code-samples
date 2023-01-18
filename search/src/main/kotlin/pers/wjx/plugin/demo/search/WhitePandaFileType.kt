package pers.wjx.plugin.demo.search

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import pers.wjx.plugin.demo.psi.common.Icons
import javax.swing.Icon

/**
 * @author wjx
 */
class WhitePandaFileType : LanguageFileType(Language.ANY) {
    companion object {
        val INSTANCE = WhitePandaFileType()
    }

    override fun getName(): String {
        return "WHITE PANDA"
    }

    override fun getDisplayName(): String {
        return "WhitePanda"
    }

    override fun getDescription(): String {
        return "White panda java file"
    }

    override fun getDefaultExtension(): String {
        return "whitePanda"
    }

    override fun getIcon(): Icon {
        return Icons.WHITE_PANDA
    }
}