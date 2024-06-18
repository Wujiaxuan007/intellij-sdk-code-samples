# IntelliJ Platform SDK Code Samples

[![知乎](https://cdn.nlark.com/yuque/0/2022/svg/1233924/1671506271562-235db770-187c-4265-a156-2ac46a13f547.svg)][wjx:zhihu]

在知乎发表了《IntelliJ IDEA 插件开发指南》反响超乎预料，考虑了阅读体感，没有细聊每个点。 知乎私信机制导致沟通非常低效，对于程序员而言，"Talk is cheap, show me the code" 才是王道

之前开发的插件 "Trantor" 暂时不考虑开源，所以只能抽出一些个人认为有必要点，以 "文章 + demo" 的形式好好聊一聊。

如果对你有帮助，记得点亮 ⭐️

## 说明

- Java 11 & Kotlin 混合开发
- Gradle 7.3
- 图标：![图标](https://cdn.nlark.com/yuque/0/2022/svg/1233924/1671960578501-62f98da8-735e-4f4a-b8af-f873500d969b.svg)

## 代码示例

> 官方也提供了简单的代码示例: [官方示例](https://github.com/JetBrains/intellij-sdk-code-samples)

未完待续 ...

| Code Sample                    | Description                                                                                                                               |
|--------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| [问题沉淀](./doc) | 开发过程中遇到的一些问题的沉淀  |
| [ProgressBar](https://github.com/Wujiaxuan007/YourProgressBar) | 可自定义的进度条 ![效果](https://user-images.githubusercontent.com/41990342/230342469-b77f1400-745b-46a5-9267-6504184e89f3.gif) |
| [PSI](./psi)                   | PSI 获取、创建、修改、删除、跳转等 <br/>![generate](https://cdn.nlark.com/yuque/0/2022/gif/1233924/1671961958581-d32c51f0-76e9-4ed5-a0e9-c9827deea23c.gif) |
| [LineMarker](./lineMarker)     | 行标记 <br/> ![lineMarkerGif](https://cdn.nlark.com/yuque/0/2022/gif/1233924/1672046795673-1e81dfcd-11a5-4729-9b3a-ba8285c44de2.gif)         |
| [Reference](./reference)       | 代码引用  <br/> ![XmlRefContributorGif](https://cdn.nlark.com/yuque/0/2022/gif/1233924/1672128287232-513197df-19cf-4ab7-9e54-97bb095d22bc.gif) |
| [Search Everywhere](./search)  | 查找 <br/> ![效果](https://cdn.nlark.com/yuque/0/2023/gif/1233924/1674029883812-dc7b2b37-7cda-4e98-a1bc-5b11a174311e.gif)|
| [ModuleType](./module)         | 自定义模块类型 ![效果](https://cdn.nlark.com/yuque/0/2023/gif/1233924/1675326998264-a25a8e81-fc95-4550-a6ac-4d0df3312677.gif)|
| [theme](https://github.com/Wujiaxuan007/jetbrains-carol-theme) | Carol 主题 ![image](https://github.com/Wujiaxuan007/intellij-sdk-code-samples/assets/41990342/86dae6b5-3308-41f5-bedf-229dd6b3f505)|
| [Compatible](./compatible_api) | // TODO sdk 兼容性问题    |
| [Error](https://github.com/Wujiaxuan007/YourProgressBar/blob/master/src/main/kotlin/pers/wjx/plugin/progress/error/ErrorSubmitter.kt) | 错误反馈，一键提交 issue 到 Github ![image](https://user-images.githubusercontent.com/41990342/230410184-ccdf1cb1-b230-4054-835e-c67985070e29.png) <br/> |

## 运行

> Gradle -> Tasks -> IntelliJ -> runIde -> Run/Debug

![运行](https://cdn.nlark.com/yuque/0/2022/png/1233924/1671960061290-24264b4c-688e-46ce-becd-776debd552c9.png?x-oss-process=image%2Fresize%2Cw_1500%2Climit_0)

## 我是如何学习的？

好几个网友问我要插件开发的学习资料，还是那句话 "官方文档、开源插件、社区、IDEA源码是插件开发最好的老师。"。

"Trantor" 插件开发周期：2020.9 到 2022.1，目前插件基本稳定，工作的重心自然也不再插件啦。 说实话，idea 插件开发与我之前的项目毫无联系，当时甚至 Java 都不太熟，当时认为它是一件有意思的事，就去做了。

开发的过程中确实有一些曲折，首先，2020 年的官方文档真的真的很晦涩(🌶️🐔)，要什么没什么。现在文档不论从内容还是排版上有质的飞跃， 所以现在才开始开发插件的你们，就偷着乐吧（doge)
。不过文档始终是无法涉及到每个点的，但是源码可以！

### 为数不多的经验之谈

> 在 IDE 看到的一切在源码里都有迹可循, 你能看到的你就能实现

**举一反三**

拿第一个需求举个例子，当时是需要做一个 Tool Window，里面是一棵资源树：
![资源树](https://plugins.jetbrains.com/files/18960/screenshot_137b1141-dad9-4cfe-8438-57d0d4a92e9a)

当时开发的大致思路：

1. 文档搜 window，定位到 `tool window` 相关内容，先大致过一遍文档，发现文档中提到 demo 项目有相关代码示例。
2. 文档搜 tree, 定位到 `tree structure` 相关内容，先大致过一遍文档，发现文档中提到 demo 项目有相关代码示例。
3. 把官方代码示例拉下来，使用 Gradle 插件提供的 runIde debug 下 `tool_window` 和 `tree_structure_provider` 的 demo，看下大致流程。
4. 其实这个需求，点类似于 Project 那个窗口，于是就代码全局搜索关键字。找到关键类后一点点 debug，参考源码里 tree & node 是如何声明的。
   ![](https://cdn.nlark.com/yuque/0/2022/png/1233924/1671524701877-d2332977-5675-4dd4-9af8-552027f66238.png)

上面 gif 可以看到下面还有一个 TrantorLint 的 tool window，主要用于代码检查（因为 Trantor 插件是服务于公司内部框架的，框架会有自己的资源定义规范与代码限制）。 当时参考了**开源**
的插件: [sonarlint](https://github.com/SonarSource/sonarlint-intellij) ，git clone 后 runIde 疯狂 debug 后的结果。

**猜猜猜**

猜，但不是盲目的猜。合理利用用关键词全局搜索，如果想要找某个方法，只能根据意图去猜。当时有个需求需要去找指定 library 里面的文件：

1. 首先要找到具体的 `library` 实体。先全局找 `Library` 类，然后在看下引用，利用关键词 "find","get" 之类的搜相关 api。
   ![lib](https://cdn.nlark.com/yuque/0/2022/png/1233924/1671528047977-bfa71b32-a16c-4d70-a43f-fea263425ee5.png?x-oss-process=image%2Fresize%2Cw_1500%2Climit_0)
2. library 实体里去找文件。这个步骤就比较简单了，`library.` 看下代码提示即可。

**注释**

学会看代码注释，这句话套在任何代码开发场景都适用。 目前官方文档仍不够细致，但大部分源码注释写的都很到位，所以还是单独拎出来引起你的重视 (doge)。


[wjx:zhihu]: https://zhuanlan.zhihu.com/p/400059601
