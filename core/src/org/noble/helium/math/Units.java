package org.noble.helium.math;

public class Units {
  public static double nanosecondsToSeconds(double nanoseconds) {
    return nanoseconds / 1000000000.0;
  }

  public static double bytesToMB(double bytes) {
    return bytes / 1024.0 / 1024.0;
  }

  public static double bytesToKB(double bytes) {
    return bytes / 1024.0 / 1024.0;
  }
}
