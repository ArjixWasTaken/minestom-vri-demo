package org.arjix.light

// Takem from https://github.com/CutestNekoAqua/MinestomBasicLight/blob/main/lib/src/main/java/minestombasiclight/SectionUtils.java

import net.minestom.server.instance.block.Block


class SectionUtils {
    private val dimension: Int

    constructor() {
        dimension = 16
    }

    constructor(dimension: Int) {
        this.dimension = dimension
    }

    /**
     * Util for light arrays
     * @param x only 0 - 16
     * @param y only 0 - 16
     * @param z only 0 - 16
     * @return index of block coordinate for light arrays
     */
    fun getCoordIndex(x: Int, y: Int, z: Int): Int {
        return y shl (dimension / 2) or (z shl (dimension / 4)) or x
    }

    fun lightCanPassThrough(block: Block): Boolean {
        return !block.isSolid || block.isAir || block.compare(Block.BARRIER)
    }

    companion object {
        //https://github.com/PaperMC/Starlight/blob/6503621c6fe1b798328a69f1bca784c6f3ffcee3/src/main/java/ca/spottedleaf/starlight/common/light/SWMRNibbleArray.java#L25
        const val ARRAY_SIZE: Int = 16 * 16 * 16 / (8 / 4) // blocks / bytes per block
    }
}