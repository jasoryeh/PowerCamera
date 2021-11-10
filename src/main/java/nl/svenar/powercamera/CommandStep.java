package nl.svenar.powercamera;

import lombok.Getter;

@Getter
public class CommandStep extends CameraStep {

  private final String command;

  public CommandStep(String unparsed) {
    super(unparsed);
    this.command = this.dataAsString();
  }

}
