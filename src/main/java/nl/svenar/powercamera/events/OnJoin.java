package nl.svenar.powercamera.events;

import java.util.List;
import java.util.Random;

import java.util.concurrent.ThreadLocalRandom;
import nl.svenar.powercamera.Util;
import nl.svenar.powercamera.commands.PowerCameraPermissions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.PowerCamera;

public class OnJoin implements Listener {

	private PowerCamera plugin;
	private Random random;

	public OnJoin(PowerCamera plugin) {
		this.plugin = plugin;
		this.random = ThreadLocalRandom.current();
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onPlayerJoin(PlayerJoinEvent e) {
		try {
			if (!e.getPlayer().hasPermission(PowerCameraPermissions.BYPASS_JOINCAMERA)) {
				if (this.plugin.getConfigCameras().addPlayer(e.getPlayer().getUniqueId()) || !this.plugin.getConfigPlugin().showOneOffCamera()) {
					List<String> joinCameras = this.plugin.getConfigPlugin().getJoinCameras();
					String camera_name = joinCameras.get(this.random.nextInt(joinCameras.size()));
					if (camera_name.length() > 0) {
						if (this.plugin.getConfigCameras().camera_exists(camera_name)) {
							this.plugin.player_camera_handler.put(e.getPlayer().getUniqueId(), new CameraHandler(plugin, e.getPlayer(), camera_name).generatePath().start());
						}
					}
				}
			}
		} catch (Exception ex) {
			// Ignore
		}

		if (Util.isPlayerInvisible(e.getPlayer())) {
			Util.setPlayerInvisible(e.getPlayer(), false);
		}
	}
}
