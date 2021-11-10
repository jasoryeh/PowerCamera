package nl.svenar.powercamera.commands;

import nl.svenar.powercamera.PowerCamera;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class cmd_stop extends PowerCameraCommand {

	public cmd_stop(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.PLAYER);
	}

	@Override
	public boolean onCommand(PowerCommandSender sender, Command cmd, String commandLabel, String[] args) {
		// guaranteed player
		if (!sender.hasPermission(PowerCameraPermissions.CMD_STOP)) {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to execute this command");
			return false;
		}
		if (this.plugin.player_camera_mode.get(((Player) sender.getSender()).getUniqueId()) != null && this.plugin.player_camera_mode.get(((Player) sender.getSender()).getUniqueId()) != PowerCamera.CAMERA_MODE.NONE && this.plugin.player_camera_handler.get(((Player) sender.getSender()).getUniqueId()) != null) {
			this.plugin.player_camera_handler.get(((Player) sender.getSender()).getUniqueId()).stop();
			if (!sender.hasPermission(PowerCameraPermissions.HIDESTARTMESSAGES)) {
				sender.sendMessage(ChatColor.GREEN + "Current camera stopped");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "No camera active!");
		}

		return false;
	}
}
