package nl.svenar.powercamera.commands;


import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.CameraManager.CameraMode;
import nl.svenar.powercamera.PowerCamera;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class cmd_preview extends PowerCameraCommand {

	public cmd_preview(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.PLAYER);
	}

	@Override
	public boolean onCommand(PowerCommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!sender.hasPermission(PowerCameraPermissions.CMD_PREVIEW)) {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to execute this command");
			return false;
		}

		CameraHandler cameraHandler = this.plugin.getCamera_manager().getCameraHandler(sender);
		if (cameraHandler != null && cameraHandler.getMode() != CameraMode.NONE) {
					sender.sendMessage(ChatColor.DARK_RED + "Camera already active!");
					return false;
		}
		if (args.length != 1) {
			sender.sendMessage(ChatColor.DARK_RED + "Usage: /" + commandLabel + " preview <point-number>");
			return false;
		}
		String camera_name = cameraHandler.getCamera_name();
		if (camera_name == null) {
			sender.sendMessage(ChatColor.RED + "No camera selected!");
			sender.sendMessage(ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
			return false;
		}

		int preview_time = plugin.getConfigPlugin().previewTime();
		int num = Integer.parseInt(args[0]) - 1;
		cameraHandler.generatePath();
		cameraHandler.preview((Player) sender.getSender(), num, preview_time);

		return false;
	}
}
