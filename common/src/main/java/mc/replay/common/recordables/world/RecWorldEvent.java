package mc.replay.common.recordables.world;

import mc.replay.common.recordables.interfaces.RecordableWorldEvent;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.play.ClientboundWorldEventPacket;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

public record RecWorldEvent(int effectId, Vector position, int data,
                            boolean disableRelativeVolume) implements RecordableWorldEvent {

    public RecWorldEvent(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(INT),
                reader.read(BLOCK_POSITION),
                reader.read(INT),
                reader.read(BOOLEAN)
        );
    }

    @Override
    public @NotNull List<@NotNull ClientboundPacket> createReplayPackets(@NotNull Function<Void, Void> function) {
        return List.of(new ClientboundWorldEventPacket(
                this.effectId,
                this.position,
                this.data,
                this.disableRelativeVolume
        ));
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(INT, this.effectId);
        writer.write(BLOCK_POSITION, this.position);
        writer.write(INT, this.data);
        writer.write(BOOLEAN, this.disableRelativeVolume);
    }
}