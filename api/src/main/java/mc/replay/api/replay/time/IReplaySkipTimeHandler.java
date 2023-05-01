package mc.replay.api.replay.time;

import mc.replay.api.replay.IReplaySession;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public interface IReplaySkipTimeHandler {

    void skipTime(@NotNull IReplaySession session, int time, @NotNull TimeUnit unit, @NotNull SkipTimeType type);
}