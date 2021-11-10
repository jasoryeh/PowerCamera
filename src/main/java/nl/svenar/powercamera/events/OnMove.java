package nl.svenar.powercamera.events;

import nl.svenar.powercamera.PowerCamera.CAMERA_MODE;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import nl.svenar.powercamera.PowerCamera;

public class OnMove implements Listener {

	private PowerCamera plugin;

	public OnMove(PowerCamera plugin) {
		this.plugin = plugin;
	}

	private boolean isWatchingPreview(Player p) {
		return plugin.player_camera_mode.get(p.getUniqueId()) == CAMERA_MODE.PREVIEW;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onPlayerMove(PlayerMoveEvent e) {
		if (this.isWatchingPreview(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
}
