package org.noble.helium;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import org.noble.helium.helpers.Coordinates;
import org.noble.helium.helpers.Dimensions;

import java.util.HashMap;

public class SimpleModelHandler {
  private static SimpleModelHandler m_instance;
  private final ModelBuilder m_modelBuilder;
  private final GLTFLoader m_modelLoader;
  private final HashMap<String, ModelInstance> m_modelInstances;

  private SimpleModelHandler() {
    m_modelBuilder = new ModelBuilder();
    m_modelInstances = new HashMap<>();
    m_modelLoader = new GLTFLoader();
  }

  public static SimpleModelHandler getInstance() {
    if(m_instance == null) {
      m_instance = new SimpleModelHandler();
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
    instance.transform.setToTranslation(coords.getVector3());

    m_modelInstances.put(name, instance);
  }

  public void addNewGLTFModel(String name, String path, Coordinates coords) {
    Model model = m_modelLoader.load(Gdx.files.internal(path)).scene.model;
    m_modelInstances.put(name, new ModelInstance(model));
    m_modelInstances.get(name).transform.setToTranslation(coords.getVector3());
  }

  public enum Shape {
    SPHERE, CUBE,
  }
}
