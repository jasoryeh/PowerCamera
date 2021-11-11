package nl.svenar.powercamera.commands;

import nl.svenar.powercamera.PowerCamera;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class cmd_addpoint extends PowerCameraCommand {

	public cmd_addpoint(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.PLAYER);
	}

	@Override
	public boolean onCommand(PowerCommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!sender.hasPermission(PowerCameraPermissions.CMD_ADDPOINT)) {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to execute this command");
			return false;
		}
		String easing = "linear";
		String camera_name = this.plugin.getCamera_manager().getCameraHandler(sender).getCamera_name();
		if (args.length == 0) {
			if (camera_name == null) {
				sender.sendMessage(ChatColor.RED + "No camera selected!");
				sender.sendMessage(ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
				return false;
			}

			plugin.getConfigCameras().camera_addpoint(((Player) sender.getSender()).getLocation(), easing, camera_name);
			sender.sendMessage(ChatColor.GREEN + "Point added to camera '" + camera_name + "'!");
		} else if (args.length == 1) {
			easing = args[0];
			if (!easing.equalsIgnoreCase("linear") && !easing.equalsIgnoreCase("teleport")) {
				sender.sendMessage(ChatColor.DARK_RED + "Usage: /" + commandLabel + " addpoint [linear/teleport]");
				return false;
			}

			if (camera_name == null) {
				sender.sendMessage(ChatColor.RED + "No camera selected!");
				sender.sendMessage(ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
				return false;
			}

			plugin.getConfigCameras().camera_addpoint(((Player) sender.getSender()).getLocation(), easing, camera_name);
			sender.sendMessage(ChatColor.GREEN + "Point added to camera '" + camera_name + "'!");

		} else {
			sender.sendMessage(ChatColor.DARK_RED + "Usage: /" + commandLabel + " addpoint [linear/teleport]");
		}

		return false;
	}
}
