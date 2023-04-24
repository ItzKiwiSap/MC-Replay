package mc.replay.common.recordables.actions.entity.action;

import mc.replay.api.recording.recordables.action.EntityRecordableAction;
import mc.replay.api.recording.recordables.data.IEntityProvider;
import mc.replay.api.recording.recordables.entity.RecordableEntityData;
import mc.replay.common.recordables.types.entity.action.RecEntitySprinting;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.play.ClientboundEntityMetadataPacket;
import mc.replay.wrapper.entity.EntityWrapper;
import mc.replay.wrapper.entity.metadata.EntityMetadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;

public record RecEntitySprintingAction() implements EntityRecordableAction<RecEntitySprinting> {

    @Override
    public @NotNull List<@NotNull ClientboundPacket> createPackets(@NotNull RecEntitySprinting recordable, @UnknownNullability IEntityProvider provider) {
        RecordableEntityData data = provider.getEntity(recordable.entityId().entityId());
        if (data == null) return List.of();

        EntityWrapper entity = data.entity();
        EntityMetadata metadata = entity.getMetadata();
        metadata.setSprinting(recordable.sprinting());

        return List.of(
                new ClientboundEntityMetadataPacket(
                        data.entityId(),
                        metadata.getEntries()
                )
        );
    }
}