package nl.svenar.powercamera.commands;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import nl.svenar.powercamera.PowerCamera;

public class MainCommand implements CommandExecutor {

	public final static HashMap<String, PowerCameraCommand> POWERCAMERA_COMMANDS = new HashMap<String, PowerCameraCommand>();

	private PowerCamera plugin;

	public MainCommand(PowerCamera plugin) {
		this.plugin = plugin;

		new cmd_help(plugin, "help");
		new cmd_create(plugin, "create");
		new cmd_remove(plugin, "remove");
		new cmd_addpoint(plugin, "addpoint");
		new cmd_addcommand(plugin, "addcommand");
		new cmd_delpoint(plugin, "delpoint");
		new cmd_select(plugin, "select");
		new cmd_preview(plugin, "preview");
		new cmd_info(plugin, "info");
		new cmd_setduration(plugin, "setduration");
		new cmd_start(plugin, "start");
		new cmd_startother(plugin, "startother");
		new cmd_stop(plugin, "stop");
		new cmd_stats(plugin, "stats");
		new cmd_invisible(plugin, "invisible");
	}

	private void showPluginInfo(CommandSender sender, String commandLabel) {
		sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + plugin.getPluginDescriptionFile().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");
		sender.sendMessage(ChatColor.GREEN + "/" + commandLabel + " help" + ChatColor.DARK_GREEN + " - For the command list.");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.DARK_GREEN + "Author: " + ChatColor.GREEN + plugin.getPluginDescriptionFile().getAuthors().get(0));
		sender.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + plugin.getPluginDescriptionFile().getVersion());
		sender.sendMessage(ChatColor.DARK_GREEN + "Website: " + ChatColor.GREEN + plugin.WEBSITE_URL);
		sender.sendMessage(ChatColor.DARK_GREEN + "Support me: " + ChatColor.YELLOW + "https://ko-fi.com/svenar");
		sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "-------------------------------" + ChatColor.BLUE + "===");
	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args.length == 0) {
			this.showPluginInfo(sender, commandLabel);
		} else {
			String command = args[0];
			PowerCameraCommand command_handler = get_powercamera_command(command);

			if (command_handler == null) {
				sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Unknown Command");
				return false;
			}

			if (!command_handler.canExecute(sender)) {
				sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Only players can use this command");
				return false;
			}

			return command_handler.onCommand(sender, cmd, commandLabel, Arrays.copyOfRange(args, 1, args.length));
		}
		return false;
	}

	public static PowerCameraCommand get_powercamera_command(String command_name) {
		return POWERCAMERA_COMMANDS.get(command_name.toLowerCase());
	}

	public static void add_powercamera_command(String command_name, PowerCameraCommand command_handler) {
		POWERCAMERA_COMMANDS.put(command_name.toLowerCase(), command_handler);
	}
}
