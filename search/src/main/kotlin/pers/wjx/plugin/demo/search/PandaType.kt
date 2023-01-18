package pers.wjx.plugin.demo.search

import pers.wjx.plugin.demo.psi.common.Icons
import javax.annotation.Nonnull
import javax.swing.Icon

/**
 * @author wjx
 */
enum class PandaType(@Nonnull val icon: Icon, @Nonnull val degree: Int) {
    /**
     * 普通大熊猫
     */
    NORMAL(Icons.PANDA, 1),

    /**
     * 白色大熊猫，白色比较稀有，degree 给 99
     */
    WHITE(Icons.WHITE_PANDA, 99)
}