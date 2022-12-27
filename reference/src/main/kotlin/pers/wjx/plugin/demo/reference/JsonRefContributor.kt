package pers.wjx.plugin.demo.reference


import com.intellij.json.psi.JsonStringLiteral
import com.intellij.json.psi.impl.JsonPropertyImpl
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.ProcessingContext
import java.util.*

/**
 *  Json 属性 key 为 panda，当值为 "xxx@??"时，"xxx" 引用类，"??"引用类下的字段。
 * "panda": "package.ClassName@age"
 *
 * @author wjx
 */
class JsonRefContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(JsonStringLiteral::class.java)
            .withParent(PlatformPatterns.psiElement(JsonPropertyImpl::class.java)),
            object : PsiReferenceProvider() {
                /**
                 * 对 jsonStringLiteral 注入额外的引用
                 * 即 jsonStringLiteral.references 多了 getReferencesByElement 返回的结果
                 */
                override fun getReferencesByElement(
                    element: PsiElement, context: ProcessingContext
                ): Array<PsiReference> {
                    val jsonStringLiteral = element as JsonStringLiteral

                    // 如果文件没有修改过，直接从缓存里拿引用
                    val userData = jsonStringLiteral.getUserData(PsiReferenceData.PANDA_REFERENCES)
                    if (userData != null && jsonStringLiteral.containingFile.modificationStamp == userData.modificationStamp) {
                        return userData.psiReferences.toTypedArray()
                    }

                    if ((jsonStringLiteral.parent as JsonPropertyImpl).value == null || (jsonStringLiteral.parent as JsonPropertyImpl).name != "panda") {
                        return PsiReference.EMPTY_ARRAY
                    }
                    val splits = jsonStringLiteral.value.split("@")
                    if (splits.size != 2) {
                        return PsiReference.EMPTY_ARRAY
                    }
                    val project = jsonStringLiteral.project

                    val classRefName = splits[0]
                    val references: ArrayList<PsiReference> = ArrayList()
                    val psiClass: PsiClass = JavaPsiFacade.getInstance(project)
                        .findClass(classRefName, GlobalSearchScope.projectScope(project))
                        ?: return PsiReference.EMPTY_ARRAY
                    references.add(PsiRef(jsonStringLiteral, TextRange(1, classRefName.length + 1), psiClass))

                    val fieldRefName = splits[1]
                    Optional.ofNullable(psiClass.findFieldByName(fieldRefName, true)).map { field ->
                        references.add(
                            PsiRef(
                                jsonStringLiteral,
                                TextRange(classRefName.length + 2, jsonStringLiteral.value.length + 1),
                                field
                            )
                        )
                    }
                    // 引用塞到缓存
                    jsonStringLiteral.putUserData(
                        PsiReferenceData.PANDA_REFERENCES,
                        PsiReferenceData(jsonStringLiteral.containingFile.modificationStamp, references)
                    )
                    return references.toTypedArray()
                }
            })
    }
}
