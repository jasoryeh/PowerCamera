package nl.svenar.powercamera.commands;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class cmd_invisible extends PowerCameraCommand {

	public cmd_invisible(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.PLAYER);
	}

	@Override
	public boolean onCommand(PowerCommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!sender.hasPermission(PowerCameraPermissions.CMD_INVISIBLE)) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "You do not have permission to execute this command");
			return false;
		}
		if (args.length != 1) {
			sender.sendMessage(ChatColor.DARK_RED + "Usage: /" + commandLabel
					+ " invisible <true/false>");
			return false;
		}
		if (!args[0].equalsIgnoreCase("true") && !args[0].equalsIgnoreCase("false")) {
			sender.sendMessage(ChatColor.DARK_RED + "Usage: /" + commandLabel
					+ " invisible <true/false>");
			return false;
		}

		boolean set_invisible = args[0].equalsIgnoreCase("true");
		Util.setPlayerInvisible(((Player) sender.getSender()), set_invisible);

		return false;
	}
}
