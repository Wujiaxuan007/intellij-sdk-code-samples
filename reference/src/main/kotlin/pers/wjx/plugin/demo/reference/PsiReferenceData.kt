package pers.wjx.plugin.demo.reference

import com.intellij.openapi.util.Key
import com.intellij.psi.PsiReference

/**
 * @author wjx
 */
class PsiReferenceData(var modificationStamp: Long, var psiReferences: List<PsiReference>) {
    companion object {
        val PANDA_REFERENCES: Key<PsiReferenceData> = Key.create("PANDA_REFERENCES")
    }
}