package mc.replay.common.dispatcher;

import net.minecraft.server.v1_16_R3.Packet;

public interface DispatcherPacketOut<T extends Packet> extends Dispatcher<T> {}