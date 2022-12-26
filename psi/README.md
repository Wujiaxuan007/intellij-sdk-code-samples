# PSI [![JetBrains IntelliJ Platform SDK Docs](https://jb.gg/badges/docs.svg)][docs]

## Quickstart

PSI 项目通过实现通过消息对话框的 "AnAction" 来演示与 PSI 相关操作:

- 创建 PSI 文件并编辑器导航定位到新建文件
    - Java 文件、方法、变量、注解
- 查找 PSI
- 修改 PSI
- 删除 PSI
- 其他：
    - Action 如何设置图标、快捷键、是否可见
    - Bundle 的实现与使用
    - 右下角小弹框（Balloon）提示实现与使用

### Actions

| ID                  | Implementation                                          | Description                                                                                                                              |
| ------------------- | ------------------------------------------------------- |----------------------------------------------------------------------------------------------------------------------------------------|
| `CreatePsiJavaFile` | [CreatePsiJavaFile][file:CreatePsiJavaFile] | 创建类并定位到新建文件 <br> ![createClass](https://cdn.nlark.com/yuque/0/2022/gif/1233924/1671961862275-b5c10893-2008-4054-a751-2ddc45240d2e.gif) |
| `GeneratePsiMethod` | [GeneratePsiMethod][file:GeneratePsiMethod] | 新建方法 <br> ![generate](https://cdn.nlark.com/yuque/0/2022/gif/1233924/1671961958581-d32c51f0-76e9-4ed5-a0e9-c9827deea23c.gif)           |
| `GeneratePsiField` | [GeneratePsiField][file:GeneratePsiField] | 新建字段 <br> ![generate](https://cdn.nlark.com/yuque/0/2022/gif/1233924/1671962069318-c96ea4e6-e3ec-4b96-b0b6-1e48e3eeb89a.gif)           |
| `EditPsiJavaFile` | [EditPsiJavaFile][file:EditPsiJavaFile] | 编辑类 <br> ![generate](https://cdn.nlark.com/yuque/0/2022/gif/1233924/1672024090711-601acf86-2a6b-4a38-9ca5-97aa5bd3d590.gif)            |
| `DeletePsiFile` | [DeletePsiFile][file:DeletePsiFile] | 删除文件 <br> ![generate](https://cdn.nlark.com/yuque/0/2022/gif/1233924/1672024247066-c5164979-211d-47dc-87a3-cc20e1acbb80.gif)           |

### Tips

- 查看文件 PSI 结构, Gradle:runIde --> Tools | View PSI Structure of Current File...
  ![viewPsi](https://cdn.nlark.com/yuque/0/2022/gif/1233924/1672025355110-acf04752-ea92-4cec-b7e3-a8fa8f85bb90.gif)
- 文件类型 `com.intellij.openapi.fileTypes.FileType`
    - [自定义文件类型](https://plugins.jetbrains.com/docs/intellij/registering-file-type.html#registration)
    - `com.intellij.psi.search.FileTypeIndex.getFiles` 可以在指定范围查找指定类型文件

[docs]: https://plugins.jetbrains.com/docs/intellij/

[docs:actions]: https://plugins.jetbrains.com/docs/intellij/psi.html

[file:CreatePsiJavaFile]: ./src/main/kotlin/pers/wjx/plugin/demo/psi/CreatePsiJavaFile.kt

[file:GeneratePsiMethod]: ./src/main/kotlin/pers/wjx/plugin/demo/psi/GeneratePsiMethod.kt

[file:GeneratePsiField]: ./src/main/kotlin/pers/wjx/plugin/demo/psi/GeneratePsiField.kt

[file:EditPsiJavaFile]: ./src/main/kotlin/pers/wjx/plugin/demo/psi/EditPsiJavaFile.kt

[file:DeletePsiFile]: ./src/main/kotlin/pers/wjx/plugin/demo/psi/DeletePsiFile.kt
