package nl.svenar.powercamera;

import java.util.HashMap;
import java.util.UUID;
import lombok.Getter;
import nl.svenar.powercamera.commands.PowerCommandSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
public class CameraManager {
  private HashMap<UUID, CameraHandler> player_camera_handler = new HashMap<UUID, CameraHandler>();

  public enum CameraMode {
    NONE, PREVIEW, VIEW
  }

  private final PowerCamera plugin;

  public CameraManager(PowerCamera plugin) {
    this.plugin = plugin;
  }

  public CameraHandler getCameraHandler(PowerCommandSender sender) {
    CommandSender sender1 = sender.getSender();
    if (sender1 instanceof Player) {
      return this.getCameraHandler(((Player) sender1).getPlayer());
    }
    return null;
  }

  public CameraHandler getCameraHandler(Player player) {
    return this.getCameraHandler(player.getUniqueId());
  }

  public CameraHandler getCameraHandler(UUID uuid) {
    if (!this.player_camera_handler.containsKey(uuid)) {
      this.player_camera_handler.put(uuid,
          new CameraHandler(this.plugin, Bukkit.getPlayer(uuid), null));
    }
    return this.player_camera_handler.get(uuid);
  }
}
