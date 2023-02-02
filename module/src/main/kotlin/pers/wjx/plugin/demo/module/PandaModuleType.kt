package pers.wjx.plugin.demo.module

import com.intellij.openapi.module.JavaModuleType
import pers.wjx.plugin.demo.module.common.Icons
import pers.wjx.plugin.demo.module.common.ModuleBundle
import javax.swing.Icon

/**
 * @author wjx
 */
object PandaModuleType : JavaModuleType(ModuleBundle.message("panda.module.id")) {

    override fun createModuleBuilder(): PandaModuleBuilder {
        return PandaModuleBuilder()
    }

    override fun getName(): String {
        return ModuleBundle.message("panda")
    }

    override fun getIcon(): Icon {
        return Icons.PANDA
    }

    override fun getDescription(): String {
        return ModuleBundle.message("panda.module.description")
    }

    override fun getNodeIcon(isOpened: Boolean): Icon {
        return Icons.PANDA
    }
}