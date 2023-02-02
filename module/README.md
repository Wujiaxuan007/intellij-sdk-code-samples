# ModuleType [![JetBrains IntelliJ Platform SDK Docs](https://jb.gg/badges/docs.svg)][docs]

自定义模块类型，用户可以在 `New Project` 或者项目文件右键`New -> Module...` 创建你所自定义的模块。

![newModule](https://cdn.nlark.com/yuque/0/2023/png/1233924/1675327309634-fc4cf088-2db6-484b-8d8b-49daba9b2bdf.png?x-oss-process=image%2Fresize%2Cw_1400%2Climit_0)

## Quickstart

> [官方示例](https://github.com/JetBrains/intellij-sdk-code-samples/tree/main/module)

### 需求

自定义 "Panda" 模块类型，基于 java 模块类型即可，不过要创建模块时要初始化 Panda 模块专属文件 "panda.yml"

1. panda.yml 为熊猫简介，包括名字、年龄、性别、照片、爱好。
2. 为了更方便的查看熊猫的照片，panda.yml 的编辑器需要提供新可视化 Tab。

  ![editro](https://cdn.nlark.com/yuque/0/2023/png/1233924/1675329665887-73fab485-3f3a-426e-b9f0-8257fb07c27d.png?x-oss-process=image%2Fresize%2Cw_1500%2Climit_0)

### 代码

| File                                                    | Description                                    |
|---------------------------------------------------------|------------------------------------------------|
| [PandaModuleType][file:PandaModuleType] | 定义模块类型，包括 ID、图标等                               |
| [PandaWizardStep][file:PandaWizardStep]                               | 熊猫简历填写步骤                                       |
| [PandaModuleBuilder][file:PandaModuleBuilder]                               | 模块构建器                                          |
| [PandaYmlEditor][file:PandaYmlEditor]                               | panda.yml 可视化"编辑器"（带双引号，因为偷懒只做了 readonly 的展示器） |

### 效果
![效果](https://cdn.nlark.com/yuque/0/2023/gif/1233924/1675326998264-a25a8e81-fc95-4550-a6ac-4d0df3312677.gif)


[docs]: https://plugins.jetbrains.com/docs/intellij/intro-project-wizard.html

[file:PandaModuleType]: ./src/main/kotlin/pers/wjx/plugin/demo/module/PandaModuleType.kt

[file:PandaWizardStep]: ./src/main/java/pers/wjx/plugin/demo/module/PandaWizardStep.java

[file:PandaModuleBuilder]: ./src/main/kotlin/pers/wjx/plugin/demo/module/PandaModuleBuilder.kt

[file:PandaYmlEditor]: ./src/main/kotlin/pers/wjx/plugin/demo/module/PandaYmlEditor.kt

[file:plugin]: ./src/main/resources/META-INF/plugin.xml
