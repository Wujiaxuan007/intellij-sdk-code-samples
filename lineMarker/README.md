# LineMarker [![JetBrains IntelliJ Platform SDK Docs](https://jb.gg/badges/docs.svg)][docs]

行标记可以用图标来标注代码。这些标记可以为相关代码提供导航目标。

![lineMarker](https://cdn.nlark.com/yuque/0/2022/png/1233924/1672046185453-ad55d33c-ef2c-4675-87a2-5618b79cda19.png)
## Quickstart

> [官方示例](https://github.com/JetBrains/intellij-sdk-code-samples/blob/main/simple_language_plugin/src/main/java/org/intellij/sdk/language/SimpleLineMarkerProvider.java)

### LineMarkerProvider
**对 Java 类行标记**
1. Xml 文件名若为类名，则添加到标记结果
2. Json 属性 key 为 panda，值为类名则添加到标记结果

| File                                                    | Description                                                                                                             |
|---------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------|
| [PandaLineMarkerProvider][file:PandaLineMarkerProvider] | ![lineMarkerGif](https://cdn.nlark.com/yuque/0/2022/gif/1233924/1672046795673-1e81dfcd-11a5-4729-9b3a-ba8285c44de2.gif) |
| [plugin.xml][file:plugin]                               | ![pluginPng](https://cdn.nlark.com/yuque/0/2022/png/1233924/1672046940359-b1dcaf8c-71bd-4ff1-a530-11e340d827ce.png)     |

### Tips

- 根据文件名查找 PsiFiles `com.intellij.psi.search.FilenameIndex.getFilesByName`

[docs]: https://plugins.jetbrains.com/docs/intellij/line-marker-provider.html#define-a-line-marker-provider

[file:PandaLineMarkerProvider]: ./src/main/kotlin/pers/wjx/plugin/demo/linemarker/PandaLineMarkerProvider.kt

[file:plugin]: ./src/main/resources/META-INF/plugin.xml