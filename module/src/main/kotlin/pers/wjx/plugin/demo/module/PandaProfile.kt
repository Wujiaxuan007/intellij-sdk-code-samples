package pers.wjx.plugin.demo.module

import com.intellij.openapi.vfs.VirtualFile

/**
 * @author wjx
 */
class PandaProfile(
    var name: String, var age: Int?, var sex: Boolean, var hobby: String, var photo: VirtualFile?
) {
    fun getImagePath(): String? {
        return photo?.path
    }

    fun getSexLabel(): String {
        return if (sex) {
            "雌"
        } else {
            "雄"
        }
    }
}