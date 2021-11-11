package nl.svenar.powercamera.events;

import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.CameraManager.CameraMode;
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
		CameraHandler cameraHandler = this.plugin.getCamera_manager().getCameraHandler(p);
		return cameraHandler != null && cameraHandler.getMode() == CameraMode.PREVIEW;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onPlayerMove(PlayerMoveEvent e) {
		if (this.isWatchingPreview(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
}
