package org.noble.helium.subsystems.telemetry;

import org.noble.helium.Constants;
import org.noble.helium.subsystems.Subsystem;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

public class HeliumTelemetry extends Subsystem {
  private static HeliumTelemetry m_instance;
  private final ArrayList<LogEntry> m_logs;
  private final ArrayList<Loggable> m_loggedItems;
  private int m_dumpInterval = Constants.Engine.Telemetry.k_defaultDumpInterval;

  private HeliumTelemetry() {
    m_logs = new ArrayList<>();
    m_loggedItems = new ArrayList<>();
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
    } else {
      printErrorln("Invalid dump interval of " + seconds);
    }
  }

  @Override
  public void update() {
    for(int currentItem = m_loggedItems.size() - 1; currentItem > 0; currentItem--) {
      ArrayList<LogEntry> entries = m_loggedItems.get(currentItem).getLogEntries();
      for(int currentEntry = entries.size() - 1; currentEntry > 0; currentEntry--) {
        LogEntry entry = entries.get(currentEntry);
        m_logs.add(new LogEntry(entry.getTimestamp(), m_loggedItems.get(currentItem).m_loggedName + "/" + entry.getItemName(), entry.getLoggedValue()));
      }
    }
  }

  @Override
  public void dispose() {

  }
}
