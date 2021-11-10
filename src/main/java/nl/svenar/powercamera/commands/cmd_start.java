package nl.svenar.powercamera.commands;

import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.PowerCamera;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class cmd_start extends PowerCameraCommand {

	public cmd_start(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.PLAYER);
	}

	@Override
	public boolean onCommand(PowerCommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args.length == 0) {
			if (sender.hasPermission(PowerCameraPermissions.CMD_START)) {
				if (this.plugin.player_camera_mode.get(((Player) sender.getSender()).getUniqueId()) == null || this.plugin.player_camera_mode.get(((Player) sender.getSender()).getUniqueId()) == PowerCamera.CAMERA_MODE.NONE) {
					String camera_name = plugin.player_selected_camera.get(((Player) sender.getSender()).getUniqueId());
					if (camera_name != null) {
						this.plugin.player_camera_handler.put(((Player) sender.getSender()).getUniqueId(), new CameraHandler(plugin, (Player) sender.getSender(), camera_name).generatePath().start());
					} else {
						sender.sendMessage(ChatColor.RED + "No camera selected!");
						sender.sendMessage(ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
					}
				} else {
					sender.sendMessage(ChatColor.DARK_RED + "Camera already active!");
				}
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "Usage: /" + commandLabel + " start");
			}

		} else if (args.length == 1) {
			String camera_name = args[0];

			if (sender.hasPermission(PowerCameraPermissions.CMD_STARTOTHER + "." + camera_name.toLowerCase())) {
				if (this.plugin.player_camera_mode.get(((Player) sender.getSender()).getUniqueId()) == null || this.plugin.player_camera_mode.get(((Player) sender.getSender()).getUniqueId()) == PowerCamera.CAMERA_MODE.NONE) {
					if (this.plugin.getConfigCameras().camera_exists(camera_name)) {
						this.plugin.player_camera_handler.put(((Player) sender.getSender()).getUniqueId(), new CameraHandler(plugin, (Player) sender.getSender(), camera_name).generatePath().start());
					} else {
						sender.sendMessage(ChatColor.RED + "Camera '" + camera_name + "' not found!");
					}
				} else {
					sender.sendMessage(ChatColor.DARK_RED + "Camera already active!");
				}
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to execute this command");
			}
		} else {
			sender.sendMessage(ChatColor.DARK_RED + "Usage: /" + commandLabel + " start");
		}

		return false;
	}
}
