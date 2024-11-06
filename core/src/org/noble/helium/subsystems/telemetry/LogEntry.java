package org.noble.helium.subsystems.telemetry;

import java.sql.Timestamp;

public class LogEntry {
  private final Timestamp m_timestamp;
  private final String m_loggedItem;
  private final String m_loggedValue;

  public LogEntry(Timestamp timestamp, String loggedItem, String loggedValue) {
    m_timestamp = timestamp;
    m_loggedItem = loggedItem;
    m_loggedValue = loggedValue;
  }

  public Timestamp getTimestamp() {
    return m_timestamp;
  }

  public String getItemName() {
    return m_loggedItem;
  }

  public String getLoggedValue() {
    return m_loggedValue;
  }
}
