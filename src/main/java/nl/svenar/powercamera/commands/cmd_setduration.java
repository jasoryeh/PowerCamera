package nl.svenar.powercamera.commands;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class cmd_setduration extends PowerCameraCommand {

	public cmd_setduration(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.PLAYER);
	}

	@Override
	public boolean onCommand(PowerCommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!sender.hasPermission(PowerCameraPermissions.CMD_SETDURATION)) {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to execute this command");
			return false;
		}
		if (args.length != 1) {
			sender.sendMessage(ChatColor.DARK_RED + "Usage: /" + commandLabel + " setduration <duration>");
			return false;
		}

		int duration = Util.timeStringToSecondsConverter(args[0]);
		if (duration <= 0) {
			sender.sendMessage(ChatColor.DARK_RED + "Duration must be greater than 0");
			return false;
		}

		String camera_name = this.plugin.getCamera_manager().getCameraHandler(((Player) sender.getSender()).getUniqueId()).getCamera_name();
		if (camera_name == null) {
			sender.sendMessage(ChatColor.RED + "No camera selected!");
			sender.sendMessage(ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
			return false;
		}

		plugin.getConfigCameras().setDuration(camera_name, duration);
		sender.sendMessage(ChatColor.GREEN + "Camera path duration set to: " + duration + " seconds on camera '" + camera_name + "'");

		return false;
	}
}
