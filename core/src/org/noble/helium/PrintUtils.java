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
        tag = ASCIIColors.YELLOW_BRIGHT + tag + ASCIIColors.RESET;
        time = ASCIIColors.YELLOW_BRIGHT + time + ASCIIColors.RESET;
        message = ASCIIColors.YELLOW_BRIGHT + message + ASCIIColors.RESET;
        printType = ASCIIColors.YELLOW_BRIGHT + printType + ASCIIColors.RESET;
        Gdx.app.log(tag + "] [" + time, printType + ": " + message);
      }
      case ERROR -> {
        tag = ASCIIColors.RED_BRIGHT + tag + ASCIIColors.RESET;
        time = ASCIIColors.RED_BRIGHT + time + ASCIIColors.RESET;
        message = ASCIIColors.RED_BRIGHT + message + ASCIIColors.RESET;
        printType = ASCIIColors.RED_BRIGHT + printType + ASCIIColors.RESET;
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
