package nl.svenar.powercamera.commands;

import nl.svenar.powercamera.PowerCamera;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;

public class cmd_remove extends PowerCameraCommand {

	public cmd_remove(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.PLAYER);
	}

	@Override
	public boolean onCommand(PowerCommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!sender.hasPermission(PowerCameraPermissions.CMD_REMOVE)) {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to execute this command");
			return false;
		}
		if (args.length != 1) {
			sender.sendMessage(ChatColor.DARK_RED + "Usage: /" + commandLabel + " remove <name>");
			return false;
		}
		String camera_name = args[0];
		if (!plugin.getConfigCameras().remove_camera(camera_name)) {
			sender.sendMessage(ChatColor.RED + "A camera with the name '" + camera_name + "' does not exists!");
			return false;
		}

		sender.sendMessage(ChatColor.GREEN + "Camera '" + camera_name + "' deleted!");

		return false;
	}
}
