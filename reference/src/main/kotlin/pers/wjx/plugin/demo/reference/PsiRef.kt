package pers.wjx.plugin.demo.reference

import com.intellij.json.psi.JsonStringLiteral
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiReferenceBase

/**
 * @param psiElement 基本元素
 * @param textRange 相对于基本元素，引用的文本范围
 * @param myResolve 引用目标元素
 *
 * @author wjx
 */
class PsiRef(psiElement: PsiElement, textRange: TextRange, private val myResolve: PsiElement) :
    PsiReferenceBase<PsiElement>(psiElement, textRange) {

    /**
     * 返回作为引用目标的元素
     */
    override fun resolve(): PsiElement {
        return myResolve
    }

    /**
     * 被引用元素重命名时的操作
     * 由于 {@link com.intellij.json.psi.JsonStringLiteralManipulator.handleContentChange} 不符合预期，需要重写下
     */
    override fun handleElementRename(newElementName: String): PsiElement {
        if (myElement is JsonStringLiteral && myResolve is PsiClass && myResolve.containingFile is PsiJavaFile) {
            return super.handleElementRename(
                (myResolve.containingFile as PsiJavaFile).packageName + "." + newElementName
            )
        }
        return super.handleElementRename(newElementName)
    }
}