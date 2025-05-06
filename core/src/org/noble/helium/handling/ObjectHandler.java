package org.noble.helium.handling;

import com.badlogic.gdx.graphics.g3d.Environment;
import org.noble.helium.PrintUtils;
import org.noble.helium.rendering.HeliumModelBatch;
import org.noble.helium.world.WorldObject;

import java.util.ArrayList;

public class ObjectHandler {
  private static ObjectHandler m_instance;
  private final ArrayList<WorldObject> m_objects;

  private ObjectHandler() {
    m_objects = new ArrayList<>();
    PrintUtils.println("Object Handler", "Object handler initialized");
  }

  public static ObjectHandler getInstance() {
    if(m_instance == null) {
      m_instance = new ObjectHandler();
    }
    return m_instance;
  }

  public void add(WorldObject object) {
    m_objects.add(object);
  }

  public ArrayList<WorldObject> getAllObjects() {
    return m_objects;
  }

  public void clear() {
    for(WorldObject object : m_objects) {
      object.dispose();
    }
    m_objects.clear();
  }

  public void update(HeliumModelBatch batch, Environment environment) {
    for(WorldObject object : m_objects) {
      object.update();
      if(object.shouldRender()) {
        batch.render(object, environment);
      }
    }
  }
}
