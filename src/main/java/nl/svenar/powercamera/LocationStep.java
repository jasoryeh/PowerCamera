package nl.svenar.powercamera;

import java.util.Arrays;
import lombok.Getter;
import org.bukkit.Location;

@Getter
public class LocationStep extends CameraStep {

  private final Location point;
  private final MovementType movementType;

  public enum MovementType {
    LINEAR,
    TELEPORT,
    UNKNOWN
  }

  public LocationStep(String unparsed) {
    super(unparsed);
    this.movementType = MovementType.valueOf(this.stepData[0].toUpperCase());
    this.point = Util.deserializeLocation(
        String.join(":",
            Arrays.copyOfRange(this.stepData, 1, this.stepData.length)));
  }

}
