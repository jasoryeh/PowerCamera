package nl.svenar.powercamera;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import nl.svenar.powercamera.CameraManager.CameraMode;
import nl.svenar.powercamera.commands.PowerCameraPermissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@Getter
public class CameraHandler {

	protected final int single_frame_duration_ms = 50;

	protected int ticks = 0;

	public final PowerCamera plugin;
	protected Player player;
	protected String camera_name;
	protected CameraMode mode = CameraMode.NONE;

	protected List<PlayableTick> camera_path = new ArrayList<PlayableTick>();

	private GameMode previous_gamemode;
	private Location previous_player_location;
	private boolean previous_invisible;

	private CameraTask runningTask;

	public CameraHandler(PowerCamera plugin, Player player, String camera_name) {
		this.plugin = plugin;
		this.player = player;
		this.setCamera_name(camera_name);
	}

	public void setCamera_name(String camera_name) {
		this.camera_name = camera_name;
		this.camera_path.clear();
		this.ticks = 0;
		this.storePlayerState();
	}

	public void appendPath(List<PlayableTick> path) {
		this.camera_path.addAll(path);
	}

	public void setPath(List<PlayableTick> path) {
		this.camera_path.clear();
		this.appendPath(path);
	}

	public void generatePath() {
		CameraPathGenerators camera_generators = this.plugin.getCamera_generators();
		this.setPath(
				camera_generators.generatePath(
						this.camera_name,
						this.single_frame_duration_ms)
		);
	}

	public void start() {
		this.storePlayerState();
		this.setPlayerState(GameMode.SPECTATOR, true);

		this.mode = CameraMode.VIEW;
		this.runningTask = new CameraTask(this);
		this.runningTask.startRepeating(1L, 1L);
		if (this.camera_path.size() > 0) {
			this.player.teleport(this.camera_path.get(0).getAt());
		}

		if (!this.player.hasPermission(PowerCameraPermissions.HIDESTARTMESSAGES)) {
			this.player.sendMessage(this.plugin.getPluginChatPrefix() + ChatColor.GREEN + "Viewing the path of camera '" + this.camera_name + "'!");
		}
	}

	public void setPlayerState(GameMode mode, Boolean invis) {
		if (mode != null && this.plugin.getConfigPlugin().shouldUseSpectator()) {
			player.setGameMode(mode);
		}
		if (invis != null && this.plugin.getConfigPlugin().shouldUseInvisibility()) {
			Util.setPlayerInvisible(this.player, invis);
		}
	}

	public void restorePlayerState() {
		player.teleport(previous_player_location);
		this.setPlayerState(this.previous_gamemode, this.previous_invisible);
	}

	public void storePlayerState() {
		this.previous_gamemode = this.player.getGameMode();
		this.previous_player_location = this.player.getLocation();
		this.previous_invisible = Util.isPlayerInvisible(this.player);
	}

	public CameraHandler stop() {
		if (this.runningTask != null && !this.runningTask.isStopped()) {
			this.runningTask.stop();
		}
		this.runningTask = null;
		return this;
	}

	private Vector calculateVelocity(Location start, Location end) {
		return new Vector(end.getX() - start.getX(), end.getY() - start.getY(), end.getZ() - start.getZ());
	}

	protected void nextFrame() {
		PlayableTick currentTick = this.camera_path.get(this.ticks);
		PlayableTick nextTick = this.camera_path.get(this.ticks);

		player.teleport(currentTick.getAt());
		for (CommandStep commandStep : currentTick.commands()) {
			String command = commandStep.getCommand().replaceAll("%player%", player.getName());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
		}

		player.setVelocity(calculateVelocity(currentTick.getAt(), nextTick.getAt()));

		this.ticks += 1;
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

		this.mode = CameraMode.PREVIEW;
		this.setPlayerState(GameMode.SPECTATOR, true);
		player.teleport(point);

		this.runningTask = new CameraTask(this);
		this.runningTask.startDelay(preview_time * 20L); // drop out of preview preview_time seconds later.
		return this;
	}

}
