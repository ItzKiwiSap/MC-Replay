package mc.replay.common.recordables.actions.entity.metadata.mob;

import mc.replay.api.recordables.action.EntityRecordableAction;
import mc.replay.api.recordables.data.IEntityProvider;
import mc.replay.api.recordables.data.RecordableEntityData;
import mc.replay.common.recordables.types.entity.metadata.mob.RecMobBaby;
import mc.replay.packetlib.data.entity.Metadata;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.play.ClientboundEntityMetadataPacket;
import mc.replay.wrapper.entity.metadata.AgeableMobMetadata;
import mc.replay.wrapper.entity.metadata.EntityMetadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;
import java.util.Map;

public record RecMobBabyAction() implements EntityRecordableAction<RecMobBaby> {

    @Override
    public @NotNull List<@NotNull ClientboundPacket> createPackets(@NotNull RecMobBaby recordable, @UnknownNullability IEntityProvider provider) {
        RecordableEntityData data = provider.getEntity(recordable.entityId().entityId());
        if (data == null) return List.of();

        EntityMetadata entityMetadata = data.entity().getMetadata();
        if (entityMetadata instanceof AgeableMobMetadata ageableMobMetadata) {
            Metadata metadata = ageableMobMetadata.getMetadata();

            metadata.detectChanges(true);

            ageableMobMetadata.setBaby(recordable.baby());

            Map<Integer, Metadata.Entry<?>> changes = metadata.getChanges();
            metadata.detectChanges(false);

            if (changes == null || changes.isEmpty()) return List.of();

            return List.of(
                    new ClientboundEntityMetadataPacket(
                            data.entityId(),
                            changes
                    )
            );
        }

        return List.of();
    }
}