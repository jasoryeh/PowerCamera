package nl.svenar.powercamera.commands;

import nl.svenar.powercamera.PowerCamera;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class cmd_select extends PowerCameraCommand {

	public cmd_select(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.PLAYER);
	}

	@Override
	public boolean onCommand(PowerCommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!sender.hasPermission(PowerCameraPermissions.CMD_SELECT)) {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to execute this command");
			return false;
		}
		if (args.length != 1) {
			sender.sendMessage(ChatColor.DARK_RED + "Usage: /" + commandLabel + " select <name>");
			return false;
		}

		String camera_name = args[0];
		if (!plugin.getConfigCameras().camera_exists(camera_name)) {
			sender.sendMessage(ChatColor.RED + "A camera with the name '" + camera_name + "' does not exists!");
			return false;
		}

		this.plugin.getCamera_manager().getCameraHandler(sender)
				.setCamera_name(plugin.getConfigCameras().get_camera_name_ignorecase(camera_name));
		sender.sendMessage(ChatColor.GREEN + "Camera '" + camera_name + "' selected!");

		return false;
	}
}
