package pers.wjx.plugin.demo.reference

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlToken
import com.intellij.psi.xml.XmlTokenType
import com.intellij.util.ProcessingContext

/**
 * Xml Tag 为 panda, 值若为项目存在的 java 全类名，则引用类。
 * <panda>package.ClassName</panda>
 *
 * @author wjx
 */
class XmlRefContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(XmlTokenType.XML_DATA_CHARACTERS),
            object : PsiReferenceProvider() {
                /**
                 * 对 token 注入额外的引用
                 * 即 token.references 多了 getReferencesByElement 返回的结果
                 */
                override fun getReferencesByElement(
                    element: PsiElement, context: ProcessingContext
                ): Array<PsiReference> {
                    val token = element as XmlToken
                    // 如果文件没有修改过，直接从缓存里拿引用
                    val userData = token.getUserData(PsiReferenceData.PANDA_REFERENCES)
                    if (userData != null && token.containingFile.modificationStamp == userData.modificationStamp) {
                        return userData.psiReferences.toTypedArray()
                    }
                    val project = element.project
                    val classRefName = token.text
                    val psiClass: PsiClass = JavaPsiFacade.getInstance(project)
                        .findClass(classRefName, GlobalSearchScope.projectScope(project))
                        ?: return PsiReference.EMPTY_ARRAY

                    val psiRef = PsiRef(
                        token, TextRange(0, classRefName.length), psiClass
                    )
                    // 引用塞到缓存
                    token.putUserData(
                        PsiReferenceData.PANDA_REFERENCES,
                        PsiReferenceData(token.containingFile.modificationStamp, arrayListOf(psiRef))
                    )
                    return arrayOf(psiRef)
                }
            })
    }
}