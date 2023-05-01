package mc.replay.common.recordables.actions.particle;

import mc.replay.api.recordables.action.EmptyRecordableAction;
import mc.replay.common.recordables.types.particle.RecParticle;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.play.ClientboundParticlePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;

public record RecParticleAction() implements EmptyRecordableAction<RecParticle> {

    @Override
    public @NotNull List<@NotNull ClientboundPacket> createPackets(@NotNull RecParticle recordable, @NotNull Void data) {
        return List.of(
                new ClientboundParticlePacket(
                        recordable.particleId(),
                        recordable.longDistance(),
                        recordable.x(),
                        recordable.y(),
                        recordable.z(),
                        recordable.offsetX(),
                        recordable.offsetY(),
                        recordable.offsetZ(),
                        recordable.particleData(),
                        recordable.particleCount(),
                        recordable.data()
                )
        );
    }

    @Override
    public @NotNull List<@NotNull ClientboundPacket> createPacketsForwards(@NotNull RecParticle recordable, @UnknownNullability Void data) {
        return List.of();
    }

    @Override
    public @NotNull List<@NotNull ClientboundPacket> createPacketsBackwards(@NotNull RecParticle recordable, @UnknownNullability Void data) {
        return List.of();
    }
}