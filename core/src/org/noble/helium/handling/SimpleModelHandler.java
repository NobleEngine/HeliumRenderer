package org.noble.helium.handling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import org.noble.helium.helpers.Dimensions;
import org.noble.helium.rendering.HeliumModelInstance;
import org.noble.helium.rendering.HeliumModelBatch;

import java.util.HashMap;
import java.util.Map;

public class SimpleModelHandler {
  private static SimpleModelHandler m_instance;
  private final ModelBuilder m_modelBuilder;
  private final GLTFLoader m_GLTFLoader;
  private final ObjLoader m_objLoader;
  private final HashMap<String, HeliumModelInstance> m_modelInstances;

  private SimpleModelHandler() {
    m_modelBuilder = new ModelBuilder();
    m_modelInstances = new HashMap<>();
    m_GLTFLoader = new GLTFLoader();
    m_objLoader = new ObjLoader();
  }

  public static SimpleModelHandler getInstance() {
    if(m_instance == null) {
      m_instance = new SimpleModelHandler();
    }
    return m_instance;
  }

  public HeliumModelInstance get(String name) {
    return m_modelInstances.get(name);
  }

  public HashMap<String, HeliumModelInstance> getModelInstances() {
    return m_modelInstances;
  }

  public void addNewShape(String name, Shape shape, Color color, Vector3 position, Dimensions dimensions) {
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

    HeliumModelInstance instance = new HeliumModelInstance(model, position);

    m_modelInstances.put(name, instance);
  }

  public void addNewShape(String name, Shape shape, Texture texture, Vector3 position, Dimensions dimensions) {
    Model model = null;

    switch(shape) {
      case CUBE -> model = m_modelBuilder.createBox(
          dimensions.getWidth(), dimensions.getHeight(), dimensions.getDepth(),
          new Material(TextureAttribute.createDiffuse(texture)),
          VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
      case SPHERE -> model = m_modelBuilder.createSphere(
          dimensions.getWidth(), dimensions.getHeight(), dimensions.getDepth(), 100, 100,
          new Material(TextureAttribute.createNormal(texture)),
          VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
    }

    HeliumModelInstance instance = new HeliumModelInstance(model, position);

    m_modelInstances.put(name, instance);
  }

  public void addNewGLTFModel(String name, String path, Vector3 position) {
    Model model = m_GLTFLoader.load(Gdx.files.internal(path)).scene.model;
    m_modelInstances.put(name, new HeliumModelInstance(model, position));
  }

  public void addNewOBJModel(String name, String path, Vector3 position) {
    Model model = m_objLoader.loadModel(Gdx.files.internal(path));
    m_modelInstances.put(name, new HeliumModelInstance(model, position));
  }

  public void setTexture(String modelName, String path) {
    HeliumModelInstance modelInstance = m_modelInstances.get(modelName);
    Texture texture = new Texture(Gdx.files.internal(path));
    Material material = new Material(TextureAttribute.createDiffuse(texture));

    modelInstance.materials.forEach(materials -> materials.set(material));
  }

  public void setColor(String modelName, Color color) {
    HeliumModelInstance modelInstance = m_modelInstances.get(modelName);
    Material material = new Material(ColorAttribute.createDiffuse(color));

    modelInstance.materials.forEach(materials -> materials.set(material));
  }

  public void setRenderable(String modelName, boolean render) {
    HeliumModelInstance modelInstance = m_modelInstances.get(modelName);
    modelInstance.setShouldRender(render);
  }

  public void render(HeliumModelBatch batch, Environment environment) {
    for (Map.Entry<String, HeliumModelInstance> entry : getInstance().getModelInstances().entrySet()) {
      if(entry.getValue().shouldRender()) {
        batch.render(entry.getValue(), environment);
      }
    }
  }

  public void clear() {
    for (Map.Entry<String, HeliumModelInstance> entry : getModelInstances().entrySet()) {
      entry.getValue().model.dispose();
    }
    m_modelInstances.clear();
  }

  public enum Shape {
    SPHERE, CUBE
  }
}
