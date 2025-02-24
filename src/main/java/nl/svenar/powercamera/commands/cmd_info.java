package nl.svenar.powercamera.commands;

import java.util.List;
import nl.svenar.powercamera.CameraStep;
import nl.svenar.powercamera.LocationStep;
import nl.svenar.powercamera.PowerCamera;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class cmd_info extends PowerCameraCommand {

	public cmd_info(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.PLAYER);
	}

	@Override
	public boolean onCommand(PowerCommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!sender.hasPermission(PowerCameraPermissions.CMD_INFO)) {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to execute this command");
			return false;
		}
		if (args.length != 0) {
			sender.sendMessage(ChatColor.DARK_RED + "Usage: /" + commandLabel + " info");
			return false;
		}
		String camera_name = this.plugin.getCamera_manager().getCameraHandler(sender).getCamera_name();
		if (camera_name == null) {
			sender.sendMessage(ChatColor.RED + "No camera selected!");
			sender.sendMessage(ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
			return false;
		}

		List<CameraStep> camera_points = plugin.getConfigCameras().getPoints(camera_name);
		int camera_duration = plugin.getConfigCameras().getDuration(camera_name);

		sender.sendRawMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + plugin.getPluginDescriptionFile().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");
		sender.sendRawMessage(ChatColor.DARK_GREEN + "Camera name: " + ChatColor.GREEN + camera_name);
		sender.sendRawMessage(ChatColor.DARK_GREEN + "Path duration: " + ChatColor.GREEN + camera_duration + " seconds");
		sender.sendRawMessage(ChatColor.DARK_GREEN + "Camera points (" + ChatColor.GREEN + camera_points.size() + ChatColor.DARK_GREEN + "):");

		int index = 0;
		for (CameraStep raw_point : camera_points) {
			index++;

			String point_info = "";
			point_info += "#" + index + " ";
			point_info += raw_point.getStepType();

			if (raw_point instanceof LocationStep) {
				Location point_location = ((LocationStep) raw_point).getPoint();

				point_info += " " + point_location.getWorld().getName();
				point_info += ", (X: " + point_location.getBlockX() + ", Y: " + point_location.getBlockY() + ", Z: " + point_location.getBlockZ() + ")";
				point_info += ", (Yaw: " + Math.round(point_location.getYaw()) + ", Pitch: " + Math.round(point_location.getPitch()) + ")";
			} else {
				point_info += ", <Raw: " + raw_point.dataAsString() + ">";
			}

			sender.sendRawMessage(ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + point_info);
		}
		sender.sendRawMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "-------------------------------" + ChatColor.BLUE + "===");

		return false;
	}
}
