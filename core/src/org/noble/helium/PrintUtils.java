package org.noble.helium;

import com.badlogic.gdx.Gdx;

import java.sql.Timestamp;
import java.time.Instant;

public class PrintUtils {
  //TODO: Implement Log4J (or something similar) and proper error handling
  public static void println(String tag, String message) {
    println(tag, message, printType.INFO);
  }

  public static void println(String tag, String message, printType type) {
    Timestamp timestamp = Timestamp.from(Instant.now());
    String time = timestamp.toString().substring(11, 19);
    String printType = type.toString();

    switch (type) {
      case INFO -> Gdx.app.log(tag + "] [" + time, printType + ": " + message);
      case WARNING -> {
        tag = ANSIColors.YELLOW_BRIGHT + tag + ANSIColors.RESET;
        time = ANSIColors.YELLOW_BRIGHT + time + ANSIColors.RESET;
        message = ANSIColors.YELLOW_BRIGHT + message + ANSIColors.RESET;
        printType = ANSIColors.YELLOW_BRIGHT + printType + ANSIColors.RESET;
        Gdx.app.log(tag + "] [" + time, printType + ": " + message);
      }
      case ERROR -> {
        tag = ANSIColors.RED_BRIGHT + tag + ANSIColors.RESET;
        time = ANSIColors.RED_BRIGHT + time + ANSIColors.RESET;
        message = ANSIColors.RED_BRIGHT + message + ANSIColors.RESET;
        printType = ANSIColors.RED_BRIGHT + printType + ANSIColors.RESET;
        Gdx.app.log(tag + "] [" + time, printType + ": " + message);
      }
    }
  }

  public static void error(String tag, Exception exception, ErrorType type, boolean printStackTrace) {
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
