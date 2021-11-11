package nl.svenar.powercamera.commands;

import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.CameraManager.CameraMode;
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
		CameraHandler cameraHandler = this.plugin.getCamera_manager().getCameraHandler(sender);
		if (args.length == 0) {
			if (sender.hasPermission(PowerCameraPermissions.CMD_START)) {
				if (cameraHandler == null || cameraHandler.getMode() == CameraMode.NONE) {
					String camera_name = cameraHandler.getCamera_name();
					if (camera_name == null) {
						sender.sendMessage(ChatColor.RED + "No camera selected!");
						sender.sendMessage(ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
					} else {
						cameraHandler.generatePath().start();
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
				if (cameraHandler == null || cameraHandler.getMode() == CameraMode.NONE) {
					if (this.plugin.getConfigCameras().camera_exists(camera_name)) {
						cameraHandler.setCamera_name(camera_name);
						cameraHandler.generatePath().start();
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
