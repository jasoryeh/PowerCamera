package nl.svenar.powercamera.commands;

import nl.svenar.powercamera.PowerCamera;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public abstract class PowerCameraCommand {
	
	protected PowerCamera plugin;
	
	enum COMMAND_EXECUTOR {
		NONE,
		PLAYER,
		CONSOLE,
		ALL
	}
	
	private COMMAND_EXECUTOR ce = COMMAND_EXECUTOR.NONE;
	
	public PowerCameraCommand(PowerCamera plugin, String command_name, COMMAND_EXECUTOR ce) {
		MainCommand.add_powercamera_command(command_name, this);
		this.plugin = plugin;
		this.ce = ce;
	}
	
	public abstract boolean onCommand(PowerCommandSender sender, Command cmd, String commandLabel, String[] args);
	
	public COMMAND_EXECUTOR getCommandExecutor() {
		return this.ce;
	}

	public boolean canExecute(CommandSender sender) {
		if (this.getCommandExecutor() == COMMAND_EXECUTOR.ALL) {
			return true;
		}

		switch (this.getCommandExecutor()) {
			case ALL:
				return true;
			case PLAYER:
				return sender instanceof Player;
			case CONSOLE:
				return sender instanceof ConsoleCommandSender;
			default:
				return false;
		}
	}

	protected String formatMessage(String msg) {
		return this.plugin.getPluginChatPrefix() + msg;
	}
}
