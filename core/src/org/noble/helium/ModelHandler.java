package org.noble.helium;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import org.noble.helium.helpers.Coordinates;
import org.noble.helium.helpers.Dimensions;

import java.util.HashMap;

public class ModelHandler {
  private static ModelHandler m_instance;
  private final ModelBuilder m_modelBuilder;
  private final HashMap<String, ModelInstance> m_modelInstances;

  private ModelHandler() {
    m_modelBuilder = new ModelBuilder();
    m_modelInstances = new HashMap<>();
  }

  public static ModelHandler getInstance() {
    if(m_instance == null) {
      m_instance = new ModelHandler();
    }
    return m_instance;
  }

  public HashMap<String, ModelInstance> getModelInstances() {
    return m_modelInstances;
  }

  public void addNewShape(String name, Shape shape, Color color, Coordinates coords, Dimensions dimensions) {
    Model model = null;

    switch(shape) {
      case CUBE -> model = m_modelBuilder.createBox(
          dimensions.getWidth(), dimensions.getHeight(), dimensions.getDepth(),
          new Material(ColorAttribute.createDiffuse(color)),
          VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
      case SPHERE -> model = m_modelBuilder.createSphere(
          dimensions.getWidth(), dimensions.getHeight(), dimensions.getDepth(), 100, 100,
          new Material(ColorAttribute.createDiffuse(color)),
          VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    }

    ModelInstance instance = new ModelInstance(model);
    instance.transform.setToTranslation(
        coords.getX(),coords.getY(),coords.getZ());

    m_modelInstances.put(name, instance);
  }

  public enum Shape {
    SPHERE, CUBE,
  }
}
