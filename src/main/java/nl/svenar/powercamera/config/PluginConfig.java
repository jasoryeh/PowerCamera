package nl.svenar.powercamera.config;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.svenar.powercamera.PowerCamera;

public class PluginConfig {

	private File configFile;
	private FileConfiguration config;

	private PowerCamera plugin;

	public PluginConfig(PowerCamera plugin) {
		this.plugin = plugin;

		this.createConfigFile();
	}

	/**
	 * Process all config data.
	 */
	public void init() {
		FileConfiguration config = this.getConfig();
		config.set("version", null);
		if (!config.isSet("camera-effects.spectator-mode"))
			config.set("camera-effects.spectator-mode", true);

		if (!config.isSet("camera-effects.invisible"))
			config.set("camera-effects.invisible", false);

		if (config.isSet("on-new-player-join-camera-path")) {
			ArrayList<String> list = new ArrayList<>();
			list.add(config.getString("on-new-player-join-camera-path"));
			config.set("on-join.random-player-camera-path", list);
			config.set("on-join.show-once", true);
			config.set("on-new-player-join-camera-path", null);
		}
		config.set("version", this.plugin.getPluginDescriptionFile().getVersion());
		this.saveConfig();
	}

	private void createConfigFile() {
		configFile = new File(plugin.getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			plugin.saveResource("config.yml", false);
		}

		config = new YamlConfiguration();
		try {
			config.load(configFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public FileConfiguration getConfig() {
		return this.config;
	}

	public void saveConfig() {
		try {
			this.config.save(this.configFile);
		} catch (IOException e) {
			plugin.getLogger().severe("Error saving " + configFile.getName());
		}
	}

	// clarity methods
	public boolean shouldUseSpectator() {
		return this.config.getBoolean("camera-effects.spectator-mode");
	}

	public boolean shouldUseInvisibility() {
		return this.config.getBoolean("camera-effects.invisible");
	}

	public int previewTime() {
		return this.config.getInt("point-preview-time");
	}

	public boolean showOneOffCamera() {
		return this.config.getBoolean("on-join.show-once");
	}

	public List<String> getJoinCameras() {
		return this.config.getStringList("on-join.random-player-camera-path");
	}
}
