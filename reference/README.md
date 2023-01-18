# Reference [![JetBrains IntelliJ Platform SDK Docs](https://jb.gg/badges/docs.svg)][docs]

> 解析引用 (resolve)，能够从 PSI 元素的使用 (访问变量、调用方法等) 导航到该元素的声明 (变量的定义、方法声明等) 。反之，获取引用 (getReferences) ，能够从该元素的声明 (变量的定义、方法声明等) 获取其他 PSI 元素的使用 (访问变量、调用方法等) 。

作为引用的 PSI 元素 (Navigate | Declaration or Usages) 都需要实现 PsiElement.getReference() 方法， 并从该方法返回 PsiReference 实现。

```java
public interface PsiElement extends UserDataHolder, Iconable {
    /**
     * 返回从这个 PSI 元素到另一个(或多个) PSI 元素(如果存在的话)的引用。
     * 如果元素有多个关联引用(示例请参阅getReferences())，则返回第一个关联引用。
     *
     * 返回: 如果PSI元素没有任何关联的引用，则为空。
     */
    @Nullable
    @Contract(pure = true)
    PsiReference getReference();

    /**
     * 返回从这个 PSI 元素到其他 PSI 元素的所有引用。
     * 一个元素可以有多个引用，例如，元素是一个包含多个子字符串的字符串，子字符串是有效的全限定类名。
     * 如果一个元素只包含一个作为引用的文本片段，但该引用有多个可能的目标，则应该使用 PsiPolyVariantReference 而不是返回多个引用。
     *
     * 实际上，更可取的方法是调用{@link PsiReferenceService#getReferences}，因为它允许在元素实现{@link ContributedReferenceHost}时通过插件添加引用。
     * 返回: 引用数组，如果元素没有关联引用则为空数组。
     */
    @Contract(pure = true)
    PsiReference @NotNull [] getReferences();
}
```

## Quickstart

### 需求

1. Xml Tag 为 panda, 值为 java 全类名，则引用类。 <panda> xxx</panda>
2. Json 属性 key 为 panda，值为 "xxx@??" 时，"xxx" 引用类，"??" 引用类下的字段。 "panda": "package.ClassNam@age"
3. 当被引用类和字段 rename 后, “xxx” 和 “??” 的值也同步修改。


### PsiReferenceContributor

| File                                          | Description                                                                                                                     |
|-----------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| [XmlRefContributor][file:XmlRefContributor]   | ![XmlRefContributorGif](https://cdn.nlark.com/yuque/0/2022/gif/1233924/1672128287232-513197df-19cf-4ab7-9e54-97bb095d22bc.gif)  |
| [JsonRefContributor][file:JsonRefContributor] | ![JsonRefContributorGif](https://cdn.nlark.com/yuque/0/2022/gif/1233924/1672128427025-a7032dbb-32f2-4408-9297-fda49cd884dd.gif) |
| [plugin.xml][file:plugin]                     | ![JsonRefContributorGif](https://cdn.nlark.com/yuque/0/2022/png/1233924/1672126428571-0bec090f-7d4e-4932-90d0-c7304d8922fa.png?x-oss-process=image%2Fresize%2Cw_1500%2Climit_0) |

[docs]: https://plugins.jetbrains.com/docs/intellij/reference-contributor.html

[file:XmlRefContributor]: ./src/main/kotlin/pers/wjx/plugin/demo/reference/XmlRefContributor.kt

[file:JsonRefContributor]: ./src/main/kotlin/pers/wjx/plugin/demo/reference/JsonRefContributor.kt

[file:plugin]: ./src/main/resources/META-INF/plugin.xml
