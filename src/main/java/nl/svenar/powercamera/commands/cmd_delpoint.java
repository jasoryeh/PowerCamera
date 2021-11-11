package nl.svenar.powercamera.commands;

import nl.svenar.powercamera.PowerCamera;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class cmd_delpoint extends PowerCameraCommand {

	public cmd_delpoint(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.PLAYER);
	}

	@Override
	public boolean onCommand(PowerCommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!sender.hasPermission(PowerCameraPermissions.CMD_DELPOINT)) {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to execute this command");
			return false;
		}
		if (args.length != 0 && args.length != 1) {
			sender.sendMessage(ChatColor.DARK_RED + "Usage: /" + commandLabel + " delpoint [point-number]");
			return false;
		}

		int num = -1;
		if (args.length == 1) {
			num = Integer.parseInt(args[0]) - 1;
		}

		String camera_name = this.plugin.getCamera_manager().getCameraHandler(sender).getCamera_name();
		if (camera_name == null) {
			sender.sendMessage(ChatColor.RED + "No camera selected!");
			sender.sendMessage(ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
			return false;
		}

		this.plugin.getConfigCameras().camera_removepoint(camera_name, num);
		sender.sendMessage(ChatColor.GREEN + "Point " + num + " removed from camera '" + camera_name + "'!");

		return false;
	}
}
