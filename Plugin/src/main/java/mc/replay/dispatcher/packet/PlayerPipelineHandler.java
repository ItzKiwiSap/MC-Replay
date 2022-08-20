package mc.replay.dispatcher.packet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import mc.replay.MCReplayPlugin;
import mc.replay.common.dispatcher.DispatcherPacketOut;
import mc.replay.common.recordables.Recordable;
import mc.replay.common.utils.reflection.nms.MinecraftPlayerNMS;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public final class PlayerPipelineHandler implements Listener {

    @Getter
    @Setter
    private boolean active = false;

    public PlayerPipelineHandler(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Channel channel = MinecraftPlayerNMS.getPacketChannel(player);

        if (channel != null) {
            PacketDispatcherPipelineListener pipelineListener = new PacketDispatcherPipelineListener(this, player);
            channel.pipeline().addBefore("packet_handler", "MC-replay", pipelineListener);
        }
    }

    @AllArgsConstructor
    public static class PacketDispatcherPipelineListener extends ChannelDuplexHandler {

        private PlayerPipelineHandler handler;
        private Player player;

//        @Override
//        public void channelRead(ChannelHandlerContext ctx, Object packetObject) throws Exception {
//            boolean logAction = this.handler.isActive() && this.player != null && this.player.isOnline();
//
//            if (logAction) try {
//                for (Map.Entry<String, ReplayPacketInConverter<? extends Recordable>> entry : MCReplayPlugin.getInstance().getPacketDispatcher().getPacketInConverters().entrySet()) {
//                    if (entry.getKey().equalsIgnoreCase(packetObject.getClass().getSimpleName())) {
//                        Recordable recordable = entry.getValue().recordableFromPacket(player, packetObject);
//                        if (recordable == null) continue;
//
//                        MCReplayPlugin.getInstance().getReplayStorage().addRecordable(recordable);
//                    }
//                }
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//
//            super.channelRead(ctx, packetObject);
//        }

        @Override
        public void write(ChannelHandlerContext ctx, Object packetObject, ChannelPromise promise) throws Exception {
            boolean logAction = this.handler.isActive() && this.player != null && this.player.isOnline();

            if (logAction) try {
                for (Map.Entry<String, DispatcherPacketOut<?>> entry : MCReplayPlugin.getInstance().getPacketDispatcher().getPacketOutConverters().entrySet()) {

                    if (entry.getKey().equalsIgnoreCase(packetObject.getClass().getSimpleName())) {
                        List<Recordable> recordables = entry.getValue().getRecordable(packetObject);
                        if (recordables == null || recordables.isEmpty()) continue;

                        for (Recordable recordable : recordables) {
                            MCReplayPlugin.getInstance().getReplayStorage().addRecordable(recordable);
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            super.write(ctx, packetObject, promise);
        }
    }
}