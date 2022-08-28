package mc.replay.dispatcher.packet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import mc.replay.MCReplayPlugin;
import mc.replay.api.recording.RecordingSession;
import mc.replay.api.recording.recordables.Recordable;
import mc.replay.common.dispatcher.DispatcherPacketIn;
import mc.replay.common.utils.reflection.nms.MinecraftPlayerNMS;
import mc.replay.recording.RecordingSessionImpl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public record PlayerPipelineListener(ReplayPacketDispatcher dispatcher) implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Channel channel = MinecraftPlayerNMS.getPacketChannel(player);

        if (channel != null) {
            PacketDispatcherPipeline pipeline = new PacketDispatcherPipeline(this, player);
            channel.pipeline().addBefore("packet_handler", "MC-replay", pipeline);
        }
    }

    @AllArgsConstructor
    public static class PacketDispatcherPipeline extends ChannelDuplexHandler {

        private PlayerPipelineListener handler;
        private Player player;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object packetObject) throws Exception {
            boolean logAction = this.handler.dispatcher().shouldRecord() && this.player != null && this.player.isOnline();

            if (logAction) try {
                for (Map.Entry<String, DispatcherPacketIn<Object>> entry : this.handler.dispatcher().getPacketInConverters().entrySet()) {
                    if (entry.getKey().equalsIgnoreCase(packetObject.getClass().getSimpleName())) {
                        List<Recordable<? extends Function<?, ?>>> recordables = entry.getValue().getRecordables(this.player, packetObject);

                        for (RecordingSession recordingSession : MCReplayPlugin.getInstance().getRecordingHandler().getRecordingSessions().values()) {
                            for (Recordable<? extends Function<?, ?>> recordable : recordables) {
                                if (recordable.shouldRecord().apply(recordingSession)) {
                                    ((RecordingSessionImpl) recordingSession).addRecordable(recordable);
                                }
                            }
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            super.channelRead(ctx, packetObject);
        }
    }
}