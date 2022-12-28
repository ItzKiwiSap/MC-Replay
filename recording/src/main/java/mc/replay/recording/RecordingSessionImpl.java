package mc.replay.recording;

import lombok.Getter;
import mc.replay.api.MCReplayAPI;
import mc.replay.api.recording.Recording;
import mc.replay.api.recording.RecordingSession;
import mc.replay.api.recording.recordables.CachedRecordable;
import mc.replay.api.recording.recordables.Recordable;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

@Getter
public final class RecordingSessionImpl implements RecordingSession {

    private final UUID sessionUuid;
    private final World world;
    private final long startTime;
    private final NavigableMap<Integer, List<CachedRecordable>> recordables;

    RecordingSessionImpl(World world) {
        this.sessionUuid = UUID.randomUUID();
        this.world = world;
        this.startTime = System.currentTimeMillis();
        this.recordables = new TreeMap<>();
    }

    @Override
    public @NotNull Recording stopRecording() {
        return MCReplayAPI.getRecordingHandler().stopRecording(this.sessionUuid);
    }

    public void addRecordables(List<Recordable<? extends Function<?, ?>>> newRecordables) {
        if (newRecordables == null || newRecordables.isEmpty()) return;

        int time = (int) (System.currentTimeMillis() - this.startTime);
        List<CachedRecordable> recordables = this.recordables.getOrDefault(time, new ArrayList<>());

        for (Recordable<? extends Function<?, ?>> newRecordable : newRecordables) {
            if (newRecordable == null) continue;

            recordables.add(new CachedRecordable(newRecordable));
        }

        this.recordables.put(time, recordables);
    }
}