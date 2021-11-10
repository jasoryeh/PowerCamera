package nl.svenar.powercamera.commands;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.TimeZone;
import nl.svenar.powercamera.PowerCamera;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class cmd_stats extends PowerCameraCommand {

	public cmd_stats(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.ALL);
	}

	private void showStats(PowerCommandSender sender) {
		if (sender.hasPermission(PowerCameraPermissions.CMD_STOP)) {
			if (this.plugin.player_camera_mode.get(((Player) sender.getSender()).getUniqueId()) != null && this.plugin.player_camera_mode.get(((Player) sender.getSender()).getUniqueId()) != PowerCamera.CAMERA_MODE.NONE && this.plugin.player_camera_handler.get(((Player) sender.getSender()).getUniqueId()) != null) {
				this.plugin.player_camera_handler.get(((Player) sender.getSender()).getUniqueId()).stop();
				if (!sender.hasPermission(PowerCameraPermissions.HIDESTARTMESSAGES)) sender.sendMessage(ChatColor.GREEN + "Current camera stopped");
			} else {
				sender.sendMessage(ChatColor.RED + "No camera active!");
			}
		} else {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to execute this command");
		}
	}

	@Override
	public boolean onCommand(PowerCommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!sender.hasPermission(PowerCameraPermissions.CMD_STATS)) {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to execute this command");
			return false;
		}

		if (args.length != 0) {
			sender.sendMessage(ChatColor.DARK_RED + "Usage: /" + commandLabel + " stats");
			return false;
		}

		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		Instant current_time = Instant.now();

		String invisibility_mode = "None";

		if (this.plugin.getConfigPlugin().shouldUseSpectator() && this.plugin.getConfigPlugin().shouldUseInvisibility()) {
			invisibility_mode = "spectator & invisible";
		} else {
			if (this.plugin.getConfigPlugin().shouldUseSpectator()) {
				invisibility_mode = "specator";
			}

			if (this.plugin.getConfigPlugin().shouldUseInvisibility()) {
				invisibility_mode = "invisible";
			}
		}

		this.showStats(sender);
		return false;
	}
}
