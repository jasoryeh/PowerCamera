package nl.svenar.powercamera.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.PowerCamera;

public class cmd_startother extends PowerCameraCommand {

	public cmd_startother(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.ALL);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args.length != 2) {
			sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel + " startother <playername> <cameraname>");
			return false;
		}

		String target_name = args[0];
		String camera_name = args[1];
		if (!sender.hasPermission(PowerCameraPermissions.CMD_STARTOTHER)) {
			sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "You do not have permission to execute this command");
			return false;
		}

		Player target_player = Bukkit.getPlayer(target_name);
		if (target_player == null) {
			sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Player '" + target_name + "' not found or is offline!");
			return false;
		}

		if (this.plugin.player_camera_mode.get(target_player.getUniqueId()) != null
				&& this.plugin.player_camera_mode.get(target_player.getUniqueId())
				!= PowerCamera.CAMERA_MODE.NONE) {
					sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Player '" + target_player.getName() + "' already has a camera active!");
					return false;
		}

		if (!this.plugin.getConfigCameras().camera_exists(camera_name)) {
			sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "Camera '" + camera_name + "' not found!");
			return false;
		}

		this.plugin.player_camera_handler.put(target_player.getUniqueId(), new CameraHandler(plugin, target_player, camera_name).generatePath().start());
		sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Playing '" + camera_name + "' on player: " + target_player.getName());

		return false;
	}
}
