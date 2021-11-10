package nl.svenar.powercamera;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import nl.svenar.powercamera.commands.MainCommand;
import nl.svenar.powercamera.config.CameraStorage;
import nl.svenar.powercamera.config.PluginConfig;
import nl.svenar.powercamera.events.ChatTabExecutor;
import nl.svenar.powercamera.events.OnJoin;
import nl.svenar.powercamera.events.OnMove;
import nl.svenar.powercamera.metrics.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class PowerCamera extends JavaPlugin {

	public final String WEBSITE_URL = "https://svenar.nl/powercamera";
	public final List<String> DONATION_URLS = Arrays.asList("https://ko-fi.com/svenar", "https://patreon.com/svenar");

	@Getter
	private PluginDescriptionFile pluginDescriptionFile;
	private String plugin_chat_prefix = ChatColor.BLACK + "[" + ChatColor.AQUA + "%plugin_name%" + ChatColor.BLACK + "] ";
	@Getter
	private PluginConfig config_plugin;
	private CameraStorage config_cameras;

	public HashMap<UUID, String> player_selected_camera = new HashMap<UUID, String>(); // Selected camera name
	public HashMap<UUID, CAMERA_MODE> player_camera_mode = new HashMap<UUID, CAMERA_MODE>(); // When the player is viewing the camera (/pc start & /pc preview)
	public HashMap<UUID, CameraHandler> player_camera_handler = new HashMap<UUID, CameraHandler>(); // When the player is viewing the camera (/pc start & /pc preview)
	public Instant powercamera_start_time = Instant.now();

	public enum CAMERA_MODE {
		NONE, PREVIEW, VIEW
	}

	public void onEnable() {
		this.pluginDescriptionFile = this.getDescription();

		this.plugin_chat_prefix = plugin_chat_prefix.replace("%plugin_name%", pluginDescriptionFile.getName());

		Bukkit.getServer().getPluginManager().registerEvents(new OnMove(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new OnJoin(this), this);
		Bukkit.getServer().getPluginCommand("powercamera").setExecutor(new MainCommand(this));
		Bukkit.getServer().getPluginCommand("powercamera").setTabCompleter(new ChatTabExecutor(this));

		this.setupConfig();

		this.getLogger().info("Enabled " + getPluginDescriptionFile().getName() + " v" + getPluginDescriptionFile().getVersion());
		this.getLogger().info("If you'd like to donate, please visit " + DONATION_URLS.get(0) + " or " + DONATION_URLS
				.get(1));

		int pluginId = 9107;
		@SuppressWarnings("unused")
		Metrics metrics = new Metrics(this, pluginId);
	}

	public void onDisable() {
		if (getLogger() != null && getPluginDescriptionFile() != null) {
			getLogger().info("Disabled " + getPluginDescriptionFile().getName() + " v" + getPluginDescriptionFile().getVersion());
		}
	}

	public String getPluginChatPrefix() {
		return this.plugin_chat_prefix;
	}

	private void setupConfig() {
		this.config_plugin = new PluginConfig(this);
		this.config_plugin.init();


		this.config_cameras = new CameraStorage(this);
		this.config_cameras.init();
	}

	public PluginConfig getConfigPlugin() {
		return this.config_plugin;
	}

	public CameraStorage getConfigCameras() {
		return this.config_cameras;
	}
}
