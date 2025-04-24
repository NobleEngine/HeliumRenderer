package org.noble.helium.subsystems.telemetry;

import org.noble.helium.Constants;
import org.noble.helium.Helium;
import org.noble.helium.subsystems.Subsystem;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;

public class HeliumTelemetry {
  //TODO: Implement Log4J (or something similar) and proper error handling
  private static HeliumTelemetry m_instance;

  private HeliumTelemetry() {
    println("Telemetry subsystem initialized on " + Constants.Engine.k_build);
  }

  public static HeliumTelemetry getInstance() {
    if(m_instance == null) {
      m_instance = new HeliumTelemetry();
    }
    return m_instance;
  }

  public void println(String string) {
    Timestamp timestamp = Timestamp.from(Instant.now());
    System.out.println(timestamp + "\t:\t" + string);
  }

  public void printErrorln(String string) {
    Timestamp timestamp = Timestamp.from(Instant.now());
    System.err.println(timestamp + "\t:\t" + string);
  }
}
