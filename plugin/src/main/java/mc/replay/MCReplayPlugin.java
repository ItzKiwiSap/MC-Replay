package mc.replay;

import lombok.Getter;
import mc.replay.api.MCReplay;
import mc.replay.api.MCReplayAPI;
import mc.replay.api.utils.config.ReplayConfigurationType;
import mc.replay.api.utils.config.SimpleConfigurationFile;
import mc.replay.classgenerator.ClassGenerator;
import mc.replay.classgenerator.objects.FakePlayerHandler;
import mc.replay.commands.ReplayTestCommand;
import mc.replay.common.recordables.RecordableRegistry;
import mc.replay.common.utils.reflection.JavaReflections;
import mc.replay.dispatcher.ReplayDispatchManager;
import mc.replay.packetlib.PacketLib;
import mc.replay.recording.RecordingHandler;
import mc.replay.replay.ReplayHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@Getter
public final class MCReplayPlugin extends JavaPlugin implements MCReplay {

    @Getter
    private static MCReplayPlugin instance;

    private PacketLib packetLib;

    private HashMap<ReplayConfigurationType, SimpleConfigurationFile> replayConfigFiles;
    private RecordingHandler recordingHandler;
    private FakePlayerHandler fakePlayerHandler;
    private RecordableRegistry recordableRegistry;

    private ReplayHandler replayHandler;

    private ReplayDispatchManager dispatchManager;

    @Override
    public void onLoad() {
        instance = this;

        JavaReflections.getField(MCReplayAPI.class, MCReplay.class, "mcReplay").set(null, this);
    }

    @Override
    public void onEnable() {
        this.packetLib = new PacketLib(this);
        this.packetLib.inject();

        //Loading all replay configuration files
        try {
            this.replayConfigFiles = new HashMap<>();
            for (ReplayConfigurationType fileType : ReplayConfigurationType.values()) {
                this.replayConfigFiles.put(fileType, new SimpleConfigurationFile(this, fileType.getFileName()));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }

        this.recordingHandler = new RecordingHandler();
        this.fakePlayerHandler = new FakePlayerHandler(this);
        this.recordableRegistry = new RecordableRegistry();

        this.replayHandler = new ReplayHandler(this);

        this.getCommand("replaytest").setExecutor(new ReplayTestCommand());

        this.dispatchManager = new ReplayDispatchManager(this);

        ClassGenerator.generate();

        this.enable();
    }

    public void enable() {
        this.dispatchManager.start();
    }

    public void disable() {
        this.dispatchManager.stop();
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public SimpleConfigurationFile getConfigFile(@NotNull ReplayConfigurationType fileType) {
        return this.replayConfigFiles.get(fileType);
    }
}