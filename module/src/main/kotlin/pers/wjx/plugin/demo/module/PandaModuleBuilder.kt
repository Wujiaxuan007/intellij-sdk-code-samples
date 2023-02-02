package pers.wjx.plugin.demo.module

import com.intellij.ide.util.projectWizard.JavaModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.module.ModifiableModuleModel
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ui.configuration.ModulesProvider
import com.intellij.util.ThrowableRunnable
import pers.wjx.plugin.demo.module.common.Icons
import pers.wjx.plugin.demo.module.common.InvokeUtil
import pers.wjx.plugin.demo.module.common.ModuleBundle
import javax.swing.Icon

/**
 * 带有 panda.yml 文件 Java 模块
 * @author wjx
 */
class PandaModuleBuilder : JavaModuleBuilder() {
    lateinit var pandaProfile: PandaProfile
    lateinit var helper: PandaModuleHelper

    /**
     * 基于 JAVA 模块类型
     */
    override fun getModuleType(): ModuleType<*> {
        return PandaModuleType
    }

    override fun getBuilderId(): String? {
        return this.javaClass.name
    }

    override fun getNodeIcon(): Icon {
        return Icons.PANDA
    }

    override fun getDescription(): String {
        return ModuleBundle.message("panda.module.description")
    }

    override fun isAvailable(): Boolean {
        return true
    }

    override fun commitModule(project: Project, model: ModifiableModuleModel?): Module? {
        InvokeUtil.runWhenInitialized(project) {
            try {
                WriteCommandAction.writeCommandAction(project).run(ThrowableRunnable<RuntimeException> {
                    helper = PandaModuleHelper(this.contentEntryPath!!)
                    initModule(project)
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return super.commitModule(project, model)
    }

    override fun createWizardSteps(
        wizardContext: WizardContext, modulesProvider: ModulesProvider
    ): Array<ModuleWizardStep> {
        val wizardSteps = super.createWizardSteps(wizardContext, modulesProvider).toMutableList()
        wizardSteps.add(PandaWizardStep(this))
        return wizardSteps.toTypedArray()
    }

    private fun initModule(project: Project) {
        helper.savaImage(pandaProfile)
        helper.createPandaYmlByFreeMarker(project, pandaProfile, "panda.yml.ftl", "panda.yml")
    }
}