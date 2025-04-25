package org.noble.helium.handling;

import org.noble.helium.subsystems.telemetry.HeliumTelemetry;
import org.noble.helium.world.WorldObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ObjectHandler {
  private static ObjectHandler m_instance;
  private final HashMap<String, WorldObject> m_objects;

  private ObjectHandler() {
    m_objects = new HashMap<>();
    HeliumTelemetry.getInstance().println("Object Handler", "Object handler initialized");
  }

  public static ObjectHandler getInstance() {
    if(m_instance == null) {
      m_instance = new ObjectHandler();
    }
    return m_instance;
  }

  public void add(String name, WorldObject object) {
    m_objects.put(name, object);
  }

  public WorldObject get(String name) {
    return m_objects.get(name);
  }

  public HashMap<String, WorldObject> getAllObjects() {
    return m_objects;
  }

  public void clear() {
    m_objects.clear();
  }

  public ArrayList<WorldObject> getCollidingObjects(WorldObject object) {
    ArrayList<WorldObject> collisions = new ArrayList<>();
    for(WorldObject obj : getAllObjects().values()) {
      if(obj.isColliding(object)) {
        collisions.add(obj);
      }
    }
    return collisions;
  }

  public void update() {
    for(WorldObject object : m_objects.values()) {
      object.update();
    }
  }
}
