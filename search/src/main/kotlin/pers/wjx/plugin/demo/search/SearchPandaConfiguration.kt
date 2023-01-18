package pers.wjx.plugin.demo.search

import com.intellij.ide.util.gotoByName.ChooseByNameFilterConfiguration
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.project.Project

/**
 * 持久化过滤器选项
 * 比如过滤器只选择了 WHITE 类型的熊猫，下次打开 SE 窗口，仍保持该选项
 *
 * @author wjx
 */
@State(name = "SearchPandaConfiguration", storages = [Storage(StoragePathMacros.WORKSPACE_FILE)])
class SearchPandaConfiguration : ChooseByNameFilterConfiguration<PandaType?>() {
    companion object {
        fun getInstance(project: Project): SearchPandaConfiguration {
            return project.getService(SearchPandaConfiguration::class.java)
        }
    }

    override fun nameForElement(type: PandaType?): String {
        return type?.name ?: PandaType.NORMAL.name
    }
}
