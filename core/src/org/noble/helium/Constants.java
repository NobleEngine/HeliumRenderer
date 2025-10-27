package org.noble.helium;

public class Constants {
  public static class Engine {
    public static final String k_prettyName = "Helium";
    public static final String k_build = "Helium-INDEV";
  }

  public static class Player {
    public static final float k_defaultSpeed = 60.0f;
    public static final float k_fastSpeed = 120.0f;
    public static final float k_jumpVerticalVelocity = 60.0f;
    public static final float k_terminalVelocity = 100.0f;
    public static final float k_gravity = (float) Math.pow(9.81,2); //9.81^2 is real gravity
  }
}
