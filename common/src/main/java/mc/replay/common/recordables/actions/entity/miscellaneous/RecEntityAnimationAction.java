package mc.replay.common.recordables.actions.entity.miscellaneous;

import mc.replay.api.recording.recordables.action.EntityRecordableAction;
import mc.replay.api.recording.recordables.data.IEntityProvider;
import mc.replay.api.recording.recordables.entity.RecordableEntityData;
import mc.replay.common.recordables.types.entity.miscellaneous.RecEntityAnimation;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.play.ClientboundEntityAnimationPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;

public record RecEntityAnimationAction() implements EntityRecordableAction<RecEntityAnimation> {

    @Override
    public @NotNull List<@NotNull ClientboundPacket> createPackets(@NotNull RecEntityAnimation recordable, @UnknownNullability IEntityProvider provider) {
        RecordableEntityData data = provider.getEntity(recordable.entityId().entityId());
        if (data == null) return List.of();

        return List.of(
                new ClientboundEntityAnimationPacket(
                        data.entityId(),
                        recordable.animation()
                )
        );
    }
}