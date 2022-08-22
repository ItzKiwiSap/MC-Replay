package mc.replay.commands;

import mc.replay.MCReplayPlugin;
import mc.replay.api.MCReplayAPI;
import mc.replay.api.recording.RecordingSession;
import mc.replay.api.recording.contestant.RecordingContestant;
import mc.replay.common.utils.color.Text;
import mc.replay.replay.session.ReplaySession;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.TreeMap;

public class ReplayTestCommand implements CommandExecutor {

    private RecordingSession session;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;

        if (!player.hasPermission("mc.replay.test")) {
            player.sendMessage(ChatColor.RED + "No permissions!");
            return false;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Please use: /replaytest <start/stop> <player>");
            return false;
        }

        if (args[0].equalsIgnoreCase("start")) {
            if (this.session != null) {
                player.sendMessage(ChatColor.RED + "A recording session is already running!");
                return false;
            }

            this.session = MCReplayAPI.getRecordingHandler().startRecording(RecordingContestant.everything());

            player.sendMessage(ChatColor.GREEN + "Recording started for everything.");
            return true;
        }

        if (args[0].equalsIgnoreCase("stop")) {
            if (this.session == null) {
                player.sendMessage(ChatColor.RED + "No recording session is running!");
                return false;
            }

            this.session.stopRecording();
            player.sendMessage(ChatColor.GREEN + "Recording stopped.");
            return true;
        }

        if (args[0].equalsIgnoreCase("play")) {
            if (this.session == null) {
                player.sendMessage(ChatColor.RED + "No recording found!");
                return false;
            }

            ReplaySession replaySession = new ReplaySession(new TreeMap<>(this.session.getRecordables()), List.of(player));
            MCReplayPlugin.getInstance().getSessions().put(player, replaySession);
            player.sendMessage(ChatColor.GREEN + "Replay session started.");
            return true;
        }

        if (args[0].equalsIgnoreCase("quit")) {
            ReplaySession replaySession = MCReplayPlugin.getInstance().getSessions().get(player);
            if (replaySession != null) {
                replaySession.stop();
                this.session = null;
                player.sendMessage(ChatColor.GREEN + "Replay stopped.");
                return true;
            }

            player.sendMessage(Text.color("&cNo active replay found."));
            return true;
        }

        return true;
    }

}