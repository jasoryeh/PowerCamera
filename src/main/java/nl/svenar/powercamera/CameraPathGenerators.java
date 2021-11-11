package nl.svenar.powercamera;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Location;

public class CameraPathGenerators {

  private final PowerCamera plugin;

  public CameraPathGenerators(PowerCamera plugin) {
    this.plugin = plugin;
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
  public List<PlayableTick> generatePath(String camera_name, int single_frame_duration_ms) {
    LinkedList<PlayableTick> path = new LinkedList<>();
    if (camera_name == null) {
      return path;
    }
    int max_points = (this.plugin.getConfigCameras().getDuration(camera_name) * 1000) / single_frame_duration_ms;

    List<CameraStep> raw_camera_points = this.plugin.getConfigCameras().getPoints(camera_name);
    List<LocationStep> raw_camera_move_points = getMovementPoints(raw_camera_points);

    Location previewLocation = raw_camera_move_points.get(0).getPoint();
    if (raw_camera_move_points.size() - 1 == 0) {
      path.addAll(Collections.nCopies(max_points - 1, PlayableTick.of(previewLocation)));
    } else {
      for (int i = 0; i < raw_camera_move_points.size() - 1; i++) {
        LocationStep raw_point = raw_camera_move_points.get(i);
        LocationStep raw_point_next = raw_camera_move_points.get(i + 1);

        path.add(PlayableTick.of(raw_point.getPoint()));
        for (int j = 0; j < max_points / (raw_camera_move_points.size() - 1) - 1; j++) {
          switch (raw_point_next.getMovementType()) {
            case LINEAR:
              path.add(
                  PlayableTick.of(translateLinear(
                      raw_point.getPoint(),
                      raw_point_next.getPoint(),
                      j,
                      max_points / (raw_camera_move_points.size() - 1) - 1)));
              break;
            case TELEPORT:
              path.add(PlayableTick.of(raw_point_next.getPoint()));
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
        path.get(index).getSubsteps().add(raw_point);
      }
    }

    return path;
  }
}
