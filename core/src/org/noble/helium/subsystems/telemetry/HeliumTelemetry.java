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

public class HeliumTelemetry extends Subsystem {
  private static HeliumTelemetry m_instance;
  private final ArrayList<LogEntry> m_logs;
  private final ArrayList<Loggable> m_loggedItems;
  private String m_logFilePath;
  private float m_dumpTimer, m_pollTimer = 0.0f;
  private int m_dumpInterval, m_pollInterval;

  private HeliumTelemetry() {
    m_logs = new ArrayList<>();
    m_loggedItems = new ArrayList<>();
    m_dumpInterval = Constants.Engine.Telemetry.k_defaultDumpInterval;
    m_pollInterval = Constants.Engine.Telemetry.k_defaultPollInterval;
    OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
    String osName = osBean.getName();
    String osArch = osBean.getArch();

    try {
      String directoryName = System.getProperty("user.home") + "\\Noble-Engine\\logs\\";
      String fileName = "log-" + LocalDate.now() + ".csv";
      File directory = new File(directoryName);
      File file = new File(directoryName + fileName);
      m_logFilePath = directoryName + fileName;

      if(directory.mkdirs()) {
        println("Created log directory. This is probably the first time this program has ran on this computer :)");
      }

      String header = "Timestamp,Item,Value";


      if(file.createNewFile()) {
        Files.write(Paths.get(directoryName + fileName), header.getBytes(), StandardOpenOption.APPEND);

        println("Log file generated");
      }
    } catch (Exception ex) {
      printErrorln(String.valueOf(ex));
    }

    println("Telemetry subsystem initialized with " + Constants.Engine.k_build + " " + osName + " " + osArch);
  }

  public static HeliumTelemetry getInstance() {
    if(m_instance == null) {
      m_instance = new HeliumTelemetry();
    }
    return m_instance;
  }

  public void println(String string) {
    Timestamp timestamp = Timestamp.from(Instant.now());
    m_logs.add(new LogEntry(timestamp, "Console", string));
    System.out.println(timestamp + " : " + string);
  }

  public void printErrorln(String string) {
    Timestamp timestamp = Timestamp.from(Instant.now());
    m_logs.add(new LogEntry(timestamp, "Console", string));
    m_logs.add(new LogEntry(timestamp, "Console/Errors", string));
    System.err.println(timestamp + " : " + string);
  }

  public void addLoggedItem(Loggable loggableItem) {
    m_loggedItems.add(loggableItem);
  }

  public void setDumpInterval(int seconds) {
    if(seconds > 0) {
      m_dumpInterval = seconds;
      println("Telemetry dump interval set to " + seconds + " seconds");
    } else {
      printErrorln("Invalid dump interval of " + seconds);
    }
  }

  public void setPollInterval(int seconds) {
    if(seconds > 0) {
      m_pollInterval = seconds;
      println("Telemetry poll interval set to " + seconds + " seconds");
    } else {
      printErrorln("Invalid poll interval of " + seconds);
    }
  }

  public void dump() {
    printErrorln("dump");
    for(int log = m_logs.size() - 1; log >= 0; log--) {
      LogEntry entry = m_logs.get(log);
      String text = "\n" + entry.getTimestamp().toString() + "," + entry.getItemName() + "," + entry.getLoggedValue();
      try {
        Files.write(Paths.get(m_logFilePath), text.getBytes(), StandardOpenOption.APPEND);
        m_logs.remove(log);
      } catch(Exception e) {
        printErrorln(e.toString());
      }
    }
  }

  @Override
  public void update() {
    m_dumpTimer += Helium.getInstance().getDelta();
    m_pollTimer += Helium.getInstance().getDelta();

    if(m_pollTimer > m_pollInterval) {
      for (int currentItem = m_loggedItems.size() - 1; currentItem >= 0; currentItem--) {
        ArrayList<LogEntry> entries = m_loggedItems.get(currentItem).getLogEntries();
        for (int currentEntry = entries.size() - 1; currentEntry >= 0; currentEntry--) {
          LogEntry entry = entries.get(currentEntry);
          m_logs.add(new LogEntry(entry.getTimestamp(), m_loggedItems.get(currentItem).m_loggedName + "/" + entry.getItemName(), entry.getLoggedValue()));
        }
      }
    }

    if(m_dumpTimer > m_dumpInterval) {
      dump();
      m_dumpTimer = 0.0f;
    }
  }

  @Override
  public void dispose() {
    dump();
  }
}
