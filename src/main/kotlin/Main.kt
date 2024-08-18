package org.arjix

import net.bladehunt.kotstom.dsl.listen
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerChunkLoadEvent
import net.minestom.server.extras.MojangAuth
import net.minestom.server.instance.block.Block
import net.minestom.server.world.DimensionType
import net.minestom.vanilla.VanillaReimplementation
import net.minestom.vanilla.generation.VanillaTestGenerator
import net.minestom.vanilla.system.RayFastManager
import org.arjix.light.LightEngine


fun main() {
    val server = MinecraftServer.init()
    val vri = VanillaReimplementation.hook(MinecraftServer.process())

    vri.process().dimension().addDimension(DimensionType.OVERWORLD)
    val overworld = vri.process().instance().createInstanceContainer(DimensionType.OVERWORLD)

    overworld.enableAutoChunkLoad(true)
    overworld.setGenerator(VanillaTestGenerator())
    overworld.loadChunk(0, 0).join()

    val lightEngine = LightEngine()
    lightEngine.recalculateInstance(overworld)

    MojangAuth.init();
    RayFastManager.init();

    vri.process().eventHandler().listen<AsyncPlayerConfigurationEvent> {
        it.spawningInstance = overworld

        var y = overworld.dimensionType.maxY
        while (Block.AIR.compare(overworld.getBlock(0, y, 0))) {
            y--
            if (y == overworld.dimensionType.minY) {
                break
            }
        }

        it.player.gameMode = GameMode.SURVIVAL
        it.player.respawnPoint = Pos(0.toDouble(), y.toDouble(), 0.toDouble())
    }

    vri.process().eventHandler().listen<PlayerChunkLoadEvent> {
        lightEngine.recalculateInstance(overworld)
    }

    server.start("127.0.0.1", 25565)
}
