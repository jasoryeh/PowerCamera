package nl.svenar.powercamera.commands;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powercamera.PowerCamera;
import org.bukkit.entity.Player;

public class cmd_stats extends PowerCameraCommand {

	public cmd_stats(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.ALL);
	}

	private void showStats(CommandSender sender) {
		if (sender.hasPermission(PowerCameraPermissions.CMD_STOP)) {
			if (this.plugin.player_camera_mode.get(((Player) sender).getUniqueId()) != null && this.plugin.player_camera_mode.get(((Player) sender).getUniqueId()) != PowerCamera.CAMERA_MODE.NONE && this.plugin.player_camera_handler.get(((Player) sender).getUniqueId()) != null) {
				this.plugin.player_camera_handler.get(((Player) sender).getUniqueId()).stop();
				if (!sender.hasPermission(PowerCameraPermissions.HIDESTARTMESSAGES)) sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Current camera stopped");
			} else {
				sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "No camera active!");
			}
		} else {
			sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "You do not have permission to execute this command");
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!sender.hasPermission(PowerCameraPermissions.CMD_STATS)) {
			sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "You do not have permission to execute this command");
			return false;
		}

		if (args.length != 0) {
			sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel + " stats");
			return false;
		}

		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		Instant current_time = Instant.now();

		String invisibility_mode = "None";

		if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.spectator-mode") && this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.invisible")) {
			invisibility_mode = "spectator & invisible";
		} else {
			if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.spectator-mode")) {
				invisibility_mode = "specator";
			}

			if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.invisible")) {
				invisibility_mode = "invisible";
			}
		}

		this.showStats(sender);
		return false;
	}
}
