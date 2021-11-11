package nl.svenar.powercamera;

import lombok.Getter;
import nl.svenar.powercamera.CameraManager.CameraMode;
import nl.svenar.powercamera.commands.PowerCameraPermissions;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CameraTask extends BukkitRunnable {

  private final CameraHandler handler;
  protected BukkitTask bukkitTask;
  @Getter
  private boolean stopped = false;

  public CameraTask(CameraHandler handler) {
    this.handler = handler;
  }

  public void startDelay(long time) {
    this.bukkitTask = this.runTaskLater(this.handler.plugin, time);
  }

  public void startRepeating(long delay, long repeatPeriod) {
    this.bukkitTask = this.runTaskTimer(this.handler.plugin, delay, repeatPeriod);
  }

  public void stop() {
    this.stop(true);
  }

  public void stop(boolean message) {
    if (this.stopped) {
      return;
    }
    this.stopped = true;
    this.handler.mode = CameraMode.NONE;
    try {
      this.cancel();
      if (this.bukkitTask != null) {
        this.bukkitTask.cancel();
      }
    } catch(Exception e) {
    }
    this.handler.restorePlayerState();
    if (message && !this.handler.player.hasPermission(PowerCameraPermissions.HIDESTARTMESSAGES)) {
      this.handler.player.sendMessage(this.handler.plugin.getPluginChatPrefix()
          + ChatColor.GREEN + "The path of camera '" + this.handler.getCamera_name() + "' has ended!");
    }
  }

  @Override
  public void run() {
    if (this.stopped) {
      return;
    }
    if (this.handler.mode == CameraMode.VIEW) {
      // viewing
      if (this.handler.ticks > this.handler.camera_path.size() - 2) {
        this.stop(); // todo
        return;
      }
      this.handler.nextFrame();
    } else {
      if (this.handler.getMode() == CameraMode.NONE) {
        return;  // no longer previewing
      }
      // preview
      this.handler.restorePlayerState();
      this.handler.mode = CameraMode.NONE;
      this.handler.player.sendMessage(this.handler.plugin.getPluginChatPrefix() + ChatColor.GREEN + "Preview ended!");
      this.stop(false);
    }
  }
}
