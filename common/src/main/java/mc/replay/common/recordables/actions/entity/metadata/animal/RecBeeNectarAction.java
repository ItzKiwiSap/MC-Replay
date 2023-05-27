package mc.replay.common.recordables.actions.entity.metadata.animal;

import mc.replay.common.recordables.actions.internal.InternalEntityMetadataRecordableAction;
import mc.replay.common.recordables.types.entity.metadata.animal.RecBeeNectar;
import mc.replay.wrapper.entity.metadata.EntityMetadata;
import mc.replay.wrapper.entity.metadata.animal.BeeMetadata;
import org.jetbrains.annotations.NotNull;

public final class RecBeeNectarAction implements InternalEntityMetadataRecordableAction<RecBeeNectar> {

    @Override
    public void writeMetadata(@NotNull RecBeeNectar recordable, @NotNull EntityMetadata entityMetadata) {
        if (entityMetadata instanceof BeeMetadata beeMetadata) {
            beeMetadata.setHasNectar(recordable.nectar());
        }
    }
}