package nl.svenar.powercamera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nl.svenar.powercamera.PowerCamera.CAMERA_MODE;
import nl.svenar.powercamera.commands.PowerCameraPermissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class CameraHandler extends BukkitRunnable {

	private int single_frame_duration_ms = 50;

	private int ticks = 0;

	private PowerCamera plugin;
	private Player player;
	private String camera_name;

	private List<Location> camera_path_points = new ArrayList<Location>();
	private Map<Integer, ArrayList<String>> camera_path_commands = new HashMap<Integer, ArrayList<String>>();

	private GameMode previous_gamemode;
	private Location previous_player_location;
	private boolean previous_invisible;

	public CameraHandler(PowerCamera plugin, Player player, String camera_name) {
		this.plugin = plugin;
		this.player = player;
		this.camera_name = camera_name;
	}

	public CameraHandler generatePath() {
		int max_points = (this.plugin.getConfigCameras().getDuration(this.camera_name) * 1000) / this.single_frame_duration_ms;

		List<CameraStep> raw_camera_points = this.plugin.getConfigCameras().getPoints(this.camera_name);
		List<LocationStep> raw_camera_move_points = getMovementPoints(raw_camera_points);

		if (raw_camera_move_points.size() - 1 == 0) {
			Location previewLocation = raw_camera_move_points.get(0).getPoint();
			this.camera_path_points.addAll(Collections.nCopies(max_points - 1, previewLocation));
		} else {
			for (int i = 0; i < raw_camera_move_points.size() - 1; i++) {
				LocationStep raw_point = raw_camera_move_points.get(i);
				LocationStep raw_point_next = raw_camera_move_points.get(i + 1);

				this.camera_path_points.add(raw_point.getPoint());
				for (int j = 0; j < max_points / (raw_camera_move_points.size() - 1) - 1; j++) {
					switch (raw_point_next.getMovementType()) {
						case LINEAR:
							this.camera_path_points.add(
									translateLinear(
											raw_point.getPoint(),
											raw_point_next.getPoint(),
											j,
											max_points / (raw_camera_move_points.size() - 1) - 1));
							break;
						case TELEPORT:
							this.camera_path_points.add(raw_point_next.getPoint());
							break;
						default:
							break;
					}
				}
			}
		}

		int command_index = 0;
		for (CameraStep raw_point : raw_camera_points) {
			if (raw_point instanceof LocationStep) {
				command_index += 1;
			}

			if (raw_point instanceof CommandStep) {
				int index = ((command_index) * max_points / (raw_camera_move_points.size()) - 1);
				index = Math.max(command_index == 0 ? 0 : index - 1, 0);
				if (!this.camera_path_commands.containsKey(index)) {
					this.camera_path_commands.put(index, new ArrayList<String>());
				}
				this.camera_path_commands.get(index).add(((CommandStep) raw_point).getCommand());
			}
		}

		return this;
	}

	private List<LocationStep> getMovementPoints(List<CameraStep> raw_camera_points) {
		return raw_camera_points.stream()
				.filter(step -> step instanceof LocationStep)
				.map(step -> (LocationStep) step)
				.collect(Collectors.toList());
	}

	private Location translateLinear(Location point, Location point_next, int progress, int progress_max) {
		if (!point.getWorld().getUID().toString().equals(point_next.getWorld().getUID().toString())) {
			return point_next;
		}

		Location new_point = new Location(point_next.getWorld(), point.getX(), point.getY(), point.getZ());

		new_point.setX(calculateProgress(point.getX(), point_next.getX(), progress, progress_max));
		new_point.setY(calculateProgress(point.getY(), point_next.getY(), progress, progress_max));
		new_point.setZ(calculateProgress(point.getZ(), point_next.getZ(), progress, progress_max));
		new_point.setYaw((float) calculateProgress(point.getYaw(), point_next.getYaw(), progress, progress_max));
		new_point.setPitch((float) calculateProgress(point.getPitch(), point_next.getPitch(), progress, progress_max));

		return new_point;
	}

	private double calculateProgress(double start, double end, int progress, int progress_max) {
		return start + ((double) progress / (double) progress_max) * (end - start);
	}

	public CameraHandler start() {
		this.storePlayerState();
		this.setCurrentState(GameMode.SPECTATOR, true);

		this.plugin.player_camera_mode.put(this.player.getUniqueId(), CAMERA_MODE.VIEW);
		runTaskTimer(this.plugin, 1L, 1L);
		if (camera_path_points.size() > 0) {
			player.teleport(camera_path_points.get(0));
		}

		if (!this.player.hasPermission(PowerCameraPermissions.HIDESTARTMESSAGES))
			this.player.sendMessage(this.plugin.getPluginChatPrefix() + ChatColor.GREEN + "Viewing the path of camera '" + this.camera_name + "'!");
		return this;
	}

	public void setCurrentState(GameMode mode, Boolean invis) {
		if (mode != null && this.plugin.getConfigPlugin().shouldUseSpectator()) {
			player.setGameMode(mode);
		}
		if (invis != null && this.plugin.getConfigPlugin().shouldUseInvisibility()) {
			Util.setPlayerInvisible(this.player, invis);
		}
	}

	public void restorePlayerState() {
		player.teleport(previous_player_location);
		this.setCurrentState(this.previous_gamemode, this.previous_invisible);
	}

	public void storePlayerState() {
		this.previous_gamemode = this.player.getGameMode();
		this.previous_player_location = this.player.getLocation();
		this.previous_invisible = Util.isPlayerInvisible(this.player);
	}

	public CameraHandler stop() {
		plugin.player_camera_mode.put(player.getUniqueId(), CAMERA_MODE.NONE);
		try {
			this.cancel();
		} catch (Exception e) {
		}

		this.restorePlayerState();

		if (!this.player.hasPermission(PowerCameraPermissions.HIDESTARTMESSAGES))
			player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "The path of camera '" + camera_name + "' has ended!");
		return this;
	}

	private Vector calculateVelocity(Location start, Location end) {
		return new Vector(end.getX() - start.getX(), end.getY() - start.getY(), end.getZ() - start.getZ());
	}

	private void nextFrame() {
		Location current_pos = camera_path_points.get(this.ticks);
		Location next_point = camera_path_points.get(this.ticks + 1);

		player.teleport(camera_path_points.get(this.ticks));

		if (camera_path_commands.containsKey(this.ticks)) {
			for (String cmd : camera_path_commands.get(this.ticks)) {
				String command = cmd.replaceAll("%player%", player.getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			}
		}

		player.setVelocity(calculateVelocity(current_pos, next_point));

		this.ticks += 1;
	}

	@Override
	public void run() {
		if (plugin.player_camera_mode.get(player.getUniqueId()) == PowerCamera.CAMERA_MODE.VIEW) {
			if (this.ticks > camera_path_points.size() - 2) {
				this.stop();
				return;
			}
			this.nextFrame();
		} else {
			if (plugin.player_camera_mode.get(player.getUniqueId()) == PowerCamera.CAMERA_MODE.NONE)
				return;
			this.restorePlayerState();
			plugin.player_camera_mode.put(player.getUniqueId(), PowerCamera.CAMERA_MODE.NONE);
			player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Preview ended!");
		}

	}

	public CameraHandler preview(Player player, int num, int preview_time) {
		List<CameraStep> camera_points = plugin.getConfigCameras().getPoints(camera_name);

		if (num < 0)
			num = 0;

		if (num > camera_points.size() - 1)
			num = camera_points.size() - 1;

		CameraStep cameraStep = camera_points.get(num);
		if (!(cameraStep instanceof LocationStep)) {
			player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "Point " + (num + 1) + " is not a location!");
			return this;
		}

		player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Preview started of point " + (num + 1) + "!");
		player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Ending in " + preview_time + " seconds.");

		previous_gamemode = player.getGameMode();
		previous_player_location = player.getLocation();
		Location point = ((LocationStep) cameraStep).getPoint();

		previous_invisible = Util.isPlayerInvisible(player);

		plugin.player_camera_mode.put(player.getUniqueId(), PowerCamera.CAMERA_MODE.PREVIEW);
		this.setCurrentState(GameMode.SPECTATOR, true);
		player.teleport(point);

		runTaskLater(this.plugin, preview_time * 20L);  // drop out of preview preview_time seconds later.
		return this;
	}

}
