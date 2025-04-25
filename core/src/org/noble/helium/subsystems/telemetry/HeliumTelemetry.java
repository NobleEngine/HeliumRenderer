package org.noble.helium.subsystems.telemetry;

import com.badlogic.gdx.Gdx;
import org.noble.helium.Constants;

import java.sql.Timestamp;
import java.time.Instant;

public class HeliumTelemetry {
  //TODO: Implement Log4J (or something similar) and proper error handling
  private static HeliumTelemetry m_instance;

  private HeliumTelemetry() {
    println("Telemetry", "Telemetry subsystem initialized on " + Constants.Engine.k_build);
  }

  public static HeliumTelemetry getInstance() {
    if(m_instance == null) {
      m_instance = new HeliumTelemetry();
    }
    return m_instance;
  }

  public void println(String tag, String message) {
    Timestamp timestamp = Timestamp.from(Instant.now());
    String time = timestamp.toString().substring(11, 19);
    Gdx.app.log(tag + "] [" + time, message);
  }

  public void printErrorln(String tag, String message) {
    Timestamp timestamp = Timestamp.from(Instant.now());
    String time = timestamp.toString().substring(11, 19);
    Gdx.app.error(tag + "] [" + time, message);
  }
}
