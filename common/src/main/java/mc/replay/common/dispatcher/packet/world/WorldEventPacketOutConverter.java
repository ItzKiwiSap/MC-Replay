package mc.replay.common.dispatcher.packet.world;

import mc.replay.api.recording.recordables.Recordable;
import mc.replay.common.dispatcher.DispatcherPacketOut;
import mc.replay.common.recordables.world.RecWorldEvent;
import mc.replay.packetlib.network.packet.clientbound.play.ClientboundWorldEventPacket;

import java.util.List;
import java.util.function.Function;

public final class WorldEventPacketOutConverter implements DispatcherPacketOut<ClientboundWorldEventPacket> {

    @Override
    public List<Recordable<? extends Function<?, ?>>> getRecordables(ClientboundWorldEventPacket packet) {
        return List.of(new RecWorldEvent(
                packet.effectId(),
                packet.position(),
                packet.data(),
                packet.disableRelativeVolume()
        ));
    }
}