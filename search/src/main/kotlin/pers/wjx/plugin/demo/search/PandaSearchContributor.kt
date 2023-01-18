package pers.wjx.plugin.demo.search

import com.intellij.ide.actions.searcheverywhere.*
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.util.ProgressIndicatorUtils
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.Processor
import com.intellij.util.containers.ContainerUtil
import org.jetbrains.annotations.NotNull
import pers.wjx.plugin.demo.search.common.SearchBundle

/**
 * panda 搜索器
 * @author wjx
 */
class PandaSearchContributor(@NotNull event: AnActionEvent) : FileSearchEverywhereContributor(event) {
    private var myFilter: PersistentSearchEverywhereContributorFilter<PandaType> =
        PersistentSearchEverywhereContributorFilter(
            PandaType.values().toList(),
            SearchPandaConfiguration.getInstance(myProject),
            PandaType::name,
            PandaType::icon
        )

    /**
     * 根据用户输入与文件名模糊匹配，匹配则加入结果集
     */
    override fun fetchWeightedElements(
        rawPattern: String, progressIndicator: ProgressIndicator, consumer: Processor<in FoundItemDescriptor<Any>>
    ) {
        if (myProject == null) {
            return
        }
        val res: MutableList<FoundItemDescriptor<Any>> = ArrayList()
        ProgressIndicatorUtils.yieldToPendingWriteActions()
        val psiManager = PsiManager.getInstance(myProject)
        ProgressIndicatorUtils.runInReadActionWithWriteActionPriority({
            // 普通熊猫
            FileTypeIndex.getFiles(PandaFileType.INSTANCE, GlobalSearchScope.projectScope(myProject)).filter {
                it.name.lowercase()
                    .contains(rawPattern.lowercase()) && myFilter.selectedElements.contains(PandaType.NORMAL)
            }.forEach {
                res.add(FoundItemDescriptor(psiManager.findFile(it), PandaType.NORMAL.degree))
            }
            // 白色熊猫
            FileTypeIndex.getFiles(WhitePandaFileType.INSTANCE, GlobalSearchScope.projectScope(myProject)).filter {
                it.name.lowercase()
                    .contains(rawPattern.lowercase()) && myFilter.selectedElements.contains(PandaType.WHITE)
            }.forEach {
                res.add(FoundItemDescriptor(psiManager.findFile(it), PandaType.WHITE.degree))
            }
            ContainerUtil.process(res, consumer)
        }, progressIndicator)
    }

    /**
     * 添加类型过滤行为
     */
    override fun getActions(onChanged: Runnable): List<AnAction?> {
        return doGetActions(myFilter, null, onChanged)
    }

    /**
     * SE 弹窗的 tab 页名称
     */
    override fun getGroupName(): String {
        return SearchBundle.message("group.PandaSearchEverywhereContributor")
    }

    /**
     * 排序权重，值越小排越前面
     */
    override fun getSortWeight(): Int {
        return 99
    }

    class Factory : SearchEverywhereContributorFactory<Any> {
        override fun createContributor(initEvent: AnActionEvent): SearchEverywhereContributor<Any> {
            return PandaSearchContributor(initEvent)
        }
    }
}