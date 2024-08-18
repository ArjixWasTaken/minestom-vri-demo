package org.arjix

import net.bladehunt.kotstom.dsl.listen
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerChunkLoadEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.extras.MojangAuth
import net.minestom.server.instance.block.Block
import net.minestom.server.world.DimensionType
import net.minestom.vanilla.VanillaReimplementation
import net.minestom.vanilla.generation.VanillaTestGenerator
import net.minestom.vanilla.system.RayFastManager
import org.arjix.events.miscEvents
import org.arjix.events.playerEvents
import org.arjix.light.LightEngine


fun main() {
    val server = MinecraftServer.init()

    // Init libraries
    MojangAuth.init();
    RayFastManager.init();

    val vri = VanillaReimplementation.hook(MinecraftServer.process())

    // register world-gen
    vri.process().dimension().addDimension(DimensionType.OVERWORLD)
    val overworld = vri.process().instance().createInstanceContainer(DimensionType.OVERWORLD).apply {
        enableAutoChunkLoad(true)
        setGenerator(VanillaTestGenerator())
    }

    // register event nodes
    vri.process().eventHandler()
        .addChild(miscEvents())
        .addChild(playerEvents(overworld))

    // Set up basic light
    LightEngine().apply {
        recalculateInstance(overworld)
        vri.process().eventHandler().listen<PlayerChunkLoadEvent> {
            recalculateInstance(overworld)
        }
    }

    server.start("127.0.0.1", 25565)
}
