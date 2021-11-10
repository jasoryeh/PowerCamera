package nl.svenar.powercamera.commands;

import lombok.Getter;
import org.bukkit.command.CommandSender;

@Getter
public class PowerCommandSender {

  private final PowerCameraCommand command;
  private final CommandSender sender;

  private PowerCommandSender(PowerCameraCommand command, CommandSender sender) {
    this.command = command;
    this.sender = sender;
  }

  public static PowerCommandSender of(PowerCameraCommand cmd, CommandSender sender) {
    return new PowerCommandSender(cmd, sender);
  }

  public void sendMessage(String msg) {
    this.sender.sendMessage(this.command.formatMessage(msg));
  }
  public void sendRawMessage(String msg) {
    this.sender.sendMessage(msg);
  }

  public boolean hasPermission(String perm) {
    return this.sender.hasPermission(perm);
  }

  public String getName() {
    return this.sender.getName();
  }
}
