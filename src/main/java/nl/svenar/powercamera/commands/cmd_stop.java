package nl.svenar.powercamera.commands;

import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.CameraManager.CameraMode;
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

		CameraHandler cameraHandler = this.plugin.getCamera_manager().getCameraHandler(sender);
		if (cameraHandler != null && cameraHandler.getMode() != CameraMode.NONE) {
			cameraHandler.stop();
			if (!sender.hasPermission(PowerCameraPermissions.HIDESTARTMESSAGES)) {
				sender.sendMessage(ChatColor.GREEN + "Current camera stopped");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "No camera active!");
		}

		return false;
	}
}
