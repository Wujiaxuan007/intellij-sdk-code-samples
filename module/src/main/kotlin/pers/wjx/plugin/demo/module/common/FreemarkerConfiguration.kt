package pers.wjx.plugin.demo.module.common

import freemarker.template.Configuration

/**
 * @author wjx
 */
class FreemarkerConfiguration(basePackagePath: String?) : Configuration(
    DEFAULT_INCOMPATIBLE_IMPROVEMENTS
) {
    init {
        defaultEncoding = "UTF-8"
        setClassForTemplateLoading(javaClass, basePackagePath)
    }
}