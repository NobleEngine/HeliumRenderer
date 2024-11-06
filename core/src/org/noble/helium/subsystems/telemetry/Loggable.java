package org.noble.helium.subsystems.telemetry;

import java.util.ArrayList;

public abstract class Loggable {
  public String m_loggedName;
  public abstract ArrayList<LogEntry> getLogs();
}
