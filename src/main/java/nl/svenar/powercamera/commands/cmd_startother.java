package nl.svenar.powercamera.commands;

import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.CameraManager.CameraMode;
import nl.svenar.powercamera.PowerCamera;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class cmd_startother extends PowerCameraCommand {

	public cmd_startother(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.ALL);
	}

	@Override
	public boolean onCommand(PowerCommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args.length != 2) {
			sender.sendMessage(ChatColor.DARK_RED + "Usage: /" + commandLabel + " startother <playername> <cameraname>");
			return false;
		}

		String target_name = args[0];
		String camera_name = args[1];
		if (!sender.hasPermission(PowerCameraPermissions.CMD_STARTOTHER)) {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to execute this command");
			return false;
		}

		Player target_player = Bukkit.getPlayer(target_name);
		if (target_player == null) {
			sender.sendMessage(ChatColor.DARK_RED + "Player '" + target_name + "' not found or is offline!");
			return false;
		}

		CameraHandler cameraHandler = this.plugin.getCamera_manager().getCameraHandler(target_player);
		if (cameraHandler != null && cameraHandler.getMode() != CameraMode.NONE) {
					sender.sendMessage(ChatColor.DARK_RED + "Player '" + target_player.getName() + "' already has a camera active!");
					return false;
		}

		if (!this.plugin.getConfigCameras().camera_exists(camera_name)) {
			sender.sendMessage(ChatColor.RED + "Camera '" + camera_name + "' not found!");
			return false;
		}

		cameraHandler.setCamera_name(camera_name);
		cameraHandler.generatePath().start();
		sender.sendMessage(ChatColor.GREEN + "Playing '" + camera_name + "' on player: " + target_player.getName());

		return false;
	}
}
