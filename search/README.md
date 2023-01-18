# Search

> Search Everywhere, 搜索资源

![se](https://resources.jetbrains.com/help/img/idea/2022.3/search_all_window.png)

## Quickstart

### 需求

**熊猫文件搜索器：**
1. 新建文件类型: Panda (.panda 后缀)、 WhitePanda（.whitePanda 后缀）
2. Search Everywhere 窗口新增 "Panda" Tab 页 ，支持搜索，支持筛选上述两种类型

![需求](https://cdn.nlark.com/yuque/0/2023/png/1233924/1674026877328-67b3dad7-5871-49c5-a099-194ed1a8523e.png?x-oss-process=image%2Fresize%2Cw_1500%2Climit_0)

### 代码

| File                                          | Description                                  |
|-----------------------------------------------|----------------------------------------------|
| [PandaFileType][file:PandaFileType]                   | 声明文件类型名为 "PANDA"，以 ".panda" 为文件后缀            |
| [WhitePandaFileType][file:WhitePandaFileType] | 声明文件类型名为 ""WHITE PANDA"，以 ".whitePanda" 为文件后缀 |
| [PandaSearchContributor][file:PandaSearchContributor] | 熊猫搜索器                                        |

### 效果

![效果](https://cdn.nlark.com/yuque/0/2023/gif/1233924/1674029883812-dc7b2b37-7cda-4e98-a1bc-5b11a174311e.gif)

[file:PandaFileType]: ./src/main/kotlin/pers/wjx/plugin/demo/search/PandaFileType.kt
[file:WhitePandaFileType]: ./src/main/kotlin/pers/wjx/plugin/demo/search/WhitePandaFileType.kt
[file:PandaSearchContributor]: ./src/main/kotlin/pers/wjx/plugin/demo/search/PandaSearchContributor.kt
