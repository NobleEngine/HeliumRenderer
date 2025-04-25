package org.noble.helium.subsystems;

import com.badlogic.gdx.Gdx;
import org.noble.helium.ConsoleColors;
import org.noble.helium.Constants;

import java.lang.management.OperatingSystemMXBean;
import java.sql.Timestamp;
import java.time.Instant;

public class HeliumTelemetry {
  //TODO: Implement Log4J (or something similar) and proper error handling
  private static HeliumTelemetry m_instance;

  private HeliumTelemetry() {
    OperatingSystemMXBean osBean = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
    println(Constants.Engine.k_prettyName, Constants.Engine.k_build + " running for " + osBean.getArch() + " " + osBean.getVersion());
    println("Telemetry", "Telemetry subsystem initialized");
  }

  public static HeliumTelemetry getInstance() {
    if (m_instance == null) {
      m_instance = new HeliumTelemetry();
    }
    return m_instance;
  }

  public void println(String tag, String message) {
    println(tag, message, printType.INFO);
  }

  public void println(String tag, String message, printType type) {
    Timestamp timestamp = Timestamp.from(Instant.now());
    String time = timestamp.toString().substring(11, 19);
    String printType = type.toString();

    switch (type) {
      case INFO -> Gdx.app.log(tag + "] [" + time, printType + ": " + message);
      case WARNING -> {
        tag = ConsoleColors.YELLOW_BRIGHT + tag + ConsoleColors.RESET;
        time = ConsoleColors.YELLOW_BRIGHT + time + ConsoleColors.RESET;
        message = ConsoleColors.YELLOW_BRIGHT + message + ConsoleColors.RESET;
        printType = ConsoleColors.YELLOW_BRIGHT + printType + ConsoleColors.RESET;
        Gdx.app.log(tag + "] [" + time, printType + ": " + message);
      }
      case ERROR -> {
        tag = ConsoleColors.RED_BRIGHT + tag + ConsoleColors.RESET;
        time = ConsoleColors.RED_BRIGHT + time + ConsoleColors.RESET;
        message = ConsoleColors.RED_BRIGHT + message + ConsoleColors.RESET;
        printType = ConsoleColors.RED_BRIGHT + printType + ConsoleColors.RESET;
        Gdx.app.log(tag + "] [" + time, printType + ": " + message);
      }
    }
  }

  public void error(String tag, Exception exception, ErrorType type, boolean printStackTrace) {
    println(tag, exception.getMessage(), printType.ERROR);
    if (printStackTrace) {
      StackTraceElement[] stackTrace = exception.getStackTrace();
      for(StackTraceElement element : stackTrace) {
        println(tag, element.toString(), printType.ERROR);
      }
    }
    if (type == ErrorType.FATAL) {
      Gdx.app.exit();
    }
  }

  public enum printType {
    INFO, WARNING, ERROR
  }

  public enum ErrorType {
    FATAL, NONFATAL
  }
}
