package org.arjix.events

import net.bladehunt.kotstom.dsl.listen
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerPacketEvent
import net.minestom.server.network.packet.client.play.ClientPlayerPositionAndRotationPacket
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket
import net.minestom.server.network.packet.client.play.ClientPlayerRotationPacket
import org.arjix.Logger

fun miscEvents(): EventNode<Event> {
    val node = EventNode.all("all")

    node.listen<PlayerPacketEvent> { event ->
        val packet = event.packet;

        if (packet is ClientPlayerPositionPacket) return@listen;
        if (packet is ClientPlayerPositionAndRotationPacket) return@listen;
        if (packet is ClientPlayerRotationPacket) return@listen;

        Logger.INSTANCE.debug("Packet received " + event.packet);
    }

    return node
}
