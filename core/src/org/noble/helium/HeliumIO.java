package org.noble.helium;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisWindow;
import org.noble.helium.subsystems.ui.UserInterface;

import java.sql.Timestamp;
import java.time.Instant;

public class HeliumIO {
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
    if (type == ErrorType.FATAL_CLOSE_GRACEFUL) {
      Gdx.app.exit();
    }
    if(type == ErrorType.FATAL_CLOSE_IMMEDIATELY) {
      System.exit(1);
    }
  }

  public static void notify(String tag, String message) {
    VisWindow window = new VisWindow(tag);
    window.add(new VisLabel(message));
    window.pack();
    window.centerWindow();
    window.setMovable(true);
    window.closeOnEscape();
    UserInterface.getInstance().addActor(window);
    Helium.getInstance().setState(Helium.State.PAUSE);
  }

  public enum printType {
    INFO, WARNING, ERROR
  }

  public enum ErrorType {
    FATAL_CLOSE_GRACEFUL, FATAL_CLOSE_IMMEDIATELY, NONFATAL
  }
}
