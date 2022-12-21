# PSI [![JetBrains IntelliJ Platform SDK Docs](https://jb.gg/badges/docs.svg)][docs:actions]

## Quickstart

PSI 项目通过实现通过消息对话框的 "AnAction" 来演示与 PSI 相关操作:

- 创建 PSI 文件并编辑器导航定位到新建文件
    - Java 文件：方法、变量
    - Json 文件：JsonArray、JsonObject
- 查找 PSI
- 修改 PSI
- 删除 PSI
- 其他：
    - Action 如何设置图标、快捷键、是否可见
    - Bundle
    - 右下角小弹框（Balloon）提示

### Actions

| ID                  | Implementation                                          | Base Action Class |
| ------------------- | ------------------------------------------------------- | ----------------- |
| `CreatePsiJavaFile` | [CreatePsiJavaFile][file:CreatePsiJavaFile] | `AnAction`        |

[docs]: https://plugins.jetbrains.com/docs/intellij/

[docs:actions]: https://plugins.jetbrains.com/docs/intellij/psi.html

[file:CreatePsiJavaFile]: ./src/main/kotlin/pers/wjx/plugin/demo/CreatePsiJavaFile.kt
