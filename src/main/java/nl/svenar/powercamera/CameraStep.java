package nl.svenar.powercamera;

import java.util.Arrays;
import lombok.Getter;

@Getter
public abstract class CameraStep {
  protected static String[] assertSplitSize(String toSplit, String at, int minSize) {
    String[] split = toSplit.split(at);
    if (split.length < minSize) {
      throw new IllegalArgumentException("Invalid movement point: " + toSplit
          + "; requires at least " + minSize
          + " arguments separated by a '" + at + "'!");
    }
    return split;
  }

  public static CameraStep deserialize(String unparsedStep) {
    String stepType = assertSplitSize(unparsedStep, ":", 1)[0];
    StepType type = StepType.valueOf(stepType.toUpperCase());
    switch (type) {
      case COMMAND:
        return new CommandStep(unparsedStep);
      case LOCATION:
        return new LocationStep(unparsedStep);
      default:
        return null;
    }
  }

  public enum StepType {
    COMMAND,
    LOCATION
  }

  protected StepType stepType;
  protected String[] stepData;

  public CameraStep(String unparsed) {
    this(assertSplitSize(unparsed, ":", 2));
  }

  public CameraStep(String[] unparsedArgs) {
    this(
        StepType.valueOf(unparsedArgs[0].toUpperCase()),
        Arrays.copyOfRange(unparsedArgs, 1, unparsedArgs.length)
    );
  }

  public CameraStep(StepType stepType, String[] stepData) {
    this.stepType = stepType;
    this.stepData = stepData;
  }

  public String dataAsString() {
    return String.join(":", stepData);
  }
}
