package org.arjix.events

import net.bladehunt.kotstom.dsl.listen
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.event.player.PlayerTickEvent
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.block.Block

fun playerEvents(instance: InstanceContainer): EventNode<Event> {
    val node = EventNode.all("all")

    fun calculateSpawnPos(spawnPos: Pos): Pos {
        instance.loadChunk(spawnPos).join()

        var y = instance.dimensionType.maxY
        while (Block.AIR.compare(instance.getBlock(spawnPos.x.toInt(), y, spawnPos.z.toInt()))) {
            y--
            if (y == instance.dimensionType.minY) {
                break
            }
        }

        return Pos(spawnPos.x, (y+1).toDouble(), spawnPos.z)
    }

    // Player joined server
    node.listen<AsyncPlayerConfigurationEvent> {
        it.spawningInstance = instance
        it.player.respawnPoint = calculateSpawnPos(Pos.ZERO)
        it.player.gameMode = GameMode.SURVIVAL
    }

    // Player spawned
    node.listen<PlayerSpawnEvent> {
        val playerInstance = it.player.instance
        playerInstance.loadChunk(it.entity.respawnPoint).join()

        it.player.teleport(calculateSpawnPos(it.entity.respawnPoint))
    }

    return node
}
