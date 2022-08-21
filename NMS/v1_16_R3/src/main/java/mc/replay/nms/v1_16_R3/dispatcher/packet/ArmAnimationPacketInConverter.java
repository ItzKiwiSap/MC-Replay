package mc.replay.nms.v1_16_R3.dispatcher.packet;

import mc.replay.common.dispatcher.DispatcherPacketIn;
import mc.replay.common.recordables.Recordable;
import mc.replay.common.replay.EntityId;
import mc.replay.common.utils.PacketConverter;
import mc.replay.nms.v1_16_R3.recordable.entity.miscellaneous.RecEntitySwingHandAnimation;
import net.minecraft.server.v1_16_R3.EnumHand;
import net.minecraft.server.v1_16_R3.PacketPlayInArmAnimation;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

public final class ArmAnimationPacketInConverter implements DispatcherPacketIn<PacketPlayInArmAnimation> {

    @Override
    public List<Recordable> getRecordables(Player player, PacketPlayInArmAnimation packet) {
        PacketConverter.ConvertedPacket convertedPacket = this.convert(packet);

        EnumHand enumHand = convertedPacket.get("a", EnumHand.class);
        EquipmentSlot hand = (enumHand.name().equalsIgnoreCase("off_hand")) ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND;

        EntityId entityId = EntityId.of(player.getUniqueId(), player.getEntityId());
        return List.of(RecEntitySwingHandAnimation.of(entityId, hand));
    }
}