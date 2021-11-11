package nl.svenar.powercamera.commands;

import nl.svenar.powercamera.PowerCamera;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class cmd_addcommand extends PowerCameraCommand {

	public cmd_addcommand(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.PLAYER);
	}

	@Override
	public boolean onCommand(PowerCommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!sender.hasPermission(PowerCameraPermissions.CMD_ADDPOINT)) {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to execute this command");
			return false;
		}
		if (args.length <= 0) {
			sender.sendMessage(ChatColor.DARK_RED + "Usage: /" + commandLabel + " addcommand <command>");
			return false;
		}
		String camera_name = this.plugin.getCamera_manager().getCameraHandler(sender).getCamera_name();
		if (camera_name == null) {
			sender.sendMessage(ChatColor.RED + "No camera selected!");
			sender.sendMessage(ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
			return false;
		}

		String command = String.join(" ", args);
		this.plugin.getConfigCameras().camera_addcommand(command, camera_name);
		sender.sendMessage(ChatColor.GREEN + "Command added to camera '" + camera_name + "'!");

		return false;
	}
}
