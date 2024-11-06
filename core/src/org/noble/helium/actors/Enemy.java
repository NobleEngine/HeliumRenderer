package org.noble.helium.actors;

import com.badlogic.gdx.math.Vector3;
import org.noble.helium.math.EulerAngles;
import org.noble.helium.rendering.HeliumModelInstance;
import org.noble.helium.subsystems.telemetry.LogEntry;

import java.util.ArrayList;

public class Enemy extends Actor {
  private final HeliumModelInstance m_model;
  private final EulerAngles m_angles;
  public Enemy(Vector3 startingPos, int startingHealth, float speed, HeliumModelInstance model, EulerAngles startingAngles) {
    super(startingPos, startingHealth, speed);
    m_model = model;
    m_angles = startingAngles;
  }

  @Override
  public void update() {
  }

  @Override
  public void dispose() {

  }

  @Override
  public ArrayList<LogEntry> getLogs() {
    return null;
  }
}
