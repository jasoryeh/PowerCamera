package nl.svenar.powercamera.events;

import java.util.ArrayList;
import java.util.List;

import nl.svenar.powercamera.commands.MainCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import nl.svenar.powercamera.PowerCamera;

public class ChatTabExecutor implements TabCompleter {

	private PowerCamera plugin;

	public ChatTabExecutor(PowerCamera plugin) {
		this.plugin = plugin;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		List<String> list = new ArrayList<String>();

		if (args.length == 1) {
			MainCommand.POWERCAMERA_COMMANDS.keySet().stream()
					.filter(command -> command.toLowerCase().contains(args[0].toLowerCase()))
					.forEach(list::add);
		}

		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("select") || args[0].equalsIgnoreCase("start")) {
				list.addAll(this.plugin.getConfigCameras().getCameras());
			}

			if (args[0].equalsIgnoreCase("invisible")) {
				list.add("true");
				list.add("false");
			}
			
			if (args[0].equalsIgnoreCase("startother")) {
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					list.add(player.getName());
				}
			}
		}
		
		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("startother")) {
				list.addAll(this.plugin.getConfigCameras().getCameras());
			}
		}

		return list;
	}

}
