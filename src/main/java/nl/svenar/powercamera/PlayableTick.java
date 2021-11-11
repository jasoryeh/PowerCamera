package nl.svenar.powercamera;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.bukkit.Location;

@Getter
public class PlayableTick {

  private final Location at;
  private final List<CameraStep> substeps = new LinkedList<>();

  public PlayableTick(Location at) {
    this.at = at;
  }

  public static PlayableTick of(Location at) {
    return new PlayableTick(at);
  }

  public List<CommandStep> commands() {
    return this.substeps.stream()
        .filter(s -> s instanceof CommandStep)
        .map(s -> ((CommandStep) s))
        .collect(Collectors.toList());
  }
}
