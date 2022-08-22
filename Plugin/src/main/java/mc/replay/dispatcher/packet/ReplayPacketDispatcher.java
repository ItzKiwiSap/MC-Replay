package mc.replay.dispatcher.packet;

import lombok.Getter;
import mc.replay.MCReplayPlugin;
import mc.replay.common.dispatcher.DispatcherPacketIn;
import mc.replay.common.dispatcher.DispatcherPacketOut;
import mc.replay.dispatcher.ReplayDispatcher;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class ReplayPacketDispatcher extends ReplayDispatcher {

    private final Map<String, DispatcherPacketIn<Object>> packetInConverters = new HashMap<>();
    private final Map<String, DispatcherPacketOut<Object>> packetOutConverters = new HashMap<>();

    public ReplayPacketDispatcher(MCReplayPlugin plugin) {
        super(plugin);

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerPipelineListener(this), plugin);
    }

    public void registerPacketInConverter(DispatcherPacketIn<Object> converter) {
        this.packetInConverters.put(converter.getInputClass().getSimpleName().toLowerCase(), converter);
    }

    public void registerPacketOutConverter(DispatcherPacketOut<Object> converter) {
        this.packetOutConverters.put(converter.getInputClass().getSimpleName().toLowerCase(), converter);
    }

    public int getDispatcherCount() {
        return this.packetInConverters.size() + this.packetOutConverters.size();
    }
}