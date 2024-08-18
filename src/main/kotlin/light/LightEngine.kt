package org.arjix.light

// Taken from https://github.com/CutestNekoAqua/MinestomBasicLight/blob/main/lib/src/main/java/minestombasiclight/LightEngine.java

import net.minestom.server.instance.Chunk
import net.minestom.server.instance.Instance
import net.minestom.server.instance.Section
import net.minestom.server.instance.block.Block
import java.util.*
import java.util.function.Consumer


/*
* Copyright Waterdev 2022, under the MIT License
*/
class LightEngine {
    private val utils: SectionUtils = SectionUtils()

    private val fullbright: Byte = 15 // 14
    private val half: Byte = 10 // 10
    private val dark: Byte = 7 // 7

    lateinit var recalcArray: ByteArray
    var exposed: Array<BooleanArray> = Array(16) { BooleanArray(16) }
    fun recalculateInstance(instance: Instance) {
        val chunks = instance.chunks.stream().toList()
        chunks.forEach((Consumer { chunk: Chunk -> this.recalculateChunk(chunk) }))
    }

    fun recalculateChunk(chunk: Chunk) {
        exposed = Array(16) { BooleanArray(16) }
        exposed.forEach { e -> Arrays.fill(e, true) }
        val sections: List<Section?> = ArrayList(chunk.sections)
        Collections.reverse(sections)
        sections.forEach(Consumer { section: Section? -> this.recalculateSection(section) })
        chunk.setBlock(1, 1, 1, chunk.getBlock(1, 1, 1))
        /*if(chunk instanceof DynamicChunk) {
                DynamicChunk dynamicChunk = (DynamicChunk) chunk;
                try {
                    Field light = dynamicChunk.getClass().getDeclaredField("blockCache");
                    light.setAccessible(true);
                    CachedPacket cachedLight = (CachedPacket) light.get(chunk);
                    cachedLight.invalidate();
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("not dynamic chunk");
            }*/
    }

    private fun recalculateSection(section: Section?) {
        recalcArray = ByteArray(ARRAY_SIZE)
        for (x in 0..15) {
            for (z in 0..15) {
                for (y in 15 downTo -1 + 1) {
                    if (!utils.lightCanPassThrough(Block.fromStateId(section!!.blockPalette()[x, y, z].toShort())!!)) exposed[x][z] =
                        false
                    if (exposed[x][z]) {
                        set(utils.getCoordIndex(x, y, z), fullbright.toInt())
                    } else {
                        set(utils.getCoordIndex(x, y, z), dark.toInt())
                    }
                    //set(utils.getCoordIndex(x,y,z), 15);
                }
            }
        }
        section!!.setSkyLight(recalcArray)
        section.setBlockLight(recalcArray)
    }

    // operation type: updating
    fun set(x: Int, y: Int, z: Int, value: Int) {
        this.set((x and 15) or ((z and 15) shl 4) or ((y and 15) shl 8), value)
    }

    // https://github.com/PaperMC/Starlight/blob/6503621c6fe1b798328a69f1bca784c6f3ffcee3/src/main/java/ca/spottedleaf/starlight/common/light/SWMRNibbleArray.java#L410
    // operation type: updating
    fun set(index: Int, value: Int) {
        val shift = (index and 1) shl 2
        val i = index ushr 1

        recalcArray[i] = ((recalcArray[i].toInt() and (0xF0 ushr shift)) or (value shl shift)).toByte()
    }

    companion object {
        //https://github.com/PaperMC/Starlight/blob/6503621c6fe1b798328a69f1bca784c6f3ffcee3/src/main/java/ca/spottedleaf/starlight/common/light/SWMRNibbleArray.java#L25
        const val ARRAY_SIZE: Int = 16 * 16 * 16 / (8 / 4) // blocks / bytes per block
    }
}
