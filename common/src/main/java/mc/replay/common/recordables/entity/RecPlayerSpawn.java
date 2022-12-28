package mc.replay.common.recordables.entity;

import mc.replay.api.recording.recordables.entity.EntityId;
import mc.replay.common.recordables.interfaces.RecordableOther;
import mc.replay.packetlib.data.Pos;
import mc.replay.packetlib.data.entity.Metadata;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.wrapper.data.SkinTexture;
import mc.replay.wrapper.entity.PlayerWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

public record RecPlayerSpawn(EntityId entityId, String name, SkinTexture skinTexture,
                             Pos position, Map<Integer, Metadata.Entry<?>> metadata) implements RecordableOther {

    public RecPlayerSpawn(EntityId entityId, PlayerWrapper playerWrapper) {
        this(
                entityId,
                playerWrapper.getUsername(),
                playerWrapper.getSkin(),
                playerWrapper.getPosition(),
                playerWrapper.getMetadata().getEntries()
        );
    }

    public RecPlayerSpawn(@NotNull ReplayByteBuffer reader) {
        this(
                new EntityId(reader),
                reader.read(STRING),
                new SkinTexture(
                        reader.read(STRING),
                        reader.read(STRING)
                ),
                new Pos(
                        reader.read(DOUBLE),
                        reader.read(DOUBLE),
                        reader.read(DOUBLE),
                        reader.read(FLOAT),
                        reader.read(FLOAT)
                ),
                readMetadata(reader)
        );
    }

    @Override
    public @NotNull List<@NotNull ClientboundPacket> createReplayPackets(@NotNull Function<Void, Void> function) {
        return List.of();
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(this.entityId);
        writer.write(STRING, this.name);
        writer.write(STRING, this.skinTexture.value());
        writer.write(STRING, this.skinTexture.signature());
        writer.write(DOUBLE, this.position.x());
        writer.write(DOUBLE, this.position.y());
        writer.write(DOUBLE, this.position.z());
        writer.write(FLOAT, this.position.yaw());
        writer.write(FLOAT, this.position.pitch());

        for (Map.Entry<Integer, Metadata.Entry<?>> entry : this.metadata.entrySet()) {
            writer.write(BYTE, entry.getKey().byteValue());
            writer.write(entry.getValue());
        }
        writer.write(BYTE, (byte) 0xFF); // End
    }

    private static Map<Integer, Metadata.Entry<?>> readMetadata(@NotNull ReplayByteBuffer reader) {
        Map<Integer, Metadata.Entry<?>> entries = new HashMap<>();
        while (true) {
            final byte index = reader.read(BYTE);
            if (index == (byte) 0xFF) break; // End
            final int type = reader.read(VAR_INT);
            entries.put((int) index, Metadata.Entry.read(type, reader));
        }
        return entries;
    }
}