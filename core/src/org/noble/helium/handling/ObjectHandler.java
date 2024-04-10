package org.noble.helium.handling;

import org.noble.helium.world.WorldObject;

import java.util.HashMap;

public class ObjectHandler {
  private static ObjectHandler m_instance;
  private final HashMap<String, WorldObject> m_objects;

  private ObjectHandler() {
    m_objects = new HashMap<>();
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
}
