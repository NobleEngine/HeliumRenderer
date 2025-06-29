package org.noble.helium.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import org.noble.helium.HeliumIO;
import org.noble.helium.math.Dimensions3;

public class HeliumModelBuilder {
  private static HeliumModelBuilder m_instance;
  private final ModelBuilder m_modelBuilder;
  private final GLTFLoader m_GLTFLoader;
  private final ObjLoader m_objLoader;

  private HeliumModelBuilder() {
    m_modelBuilder = new ModelBuilder();
    m_GLTFLoader = new GLTFLoader();
    m_objLoader = new ObjLoader();
  }

  public static HeliumModelBuilder getInstance() {
    if(m_instance == null) {
      m_instance = new HeliumModelBuilder();
    }
    return m_instance;
  }

  public Model create(Shape shape, Texture texture, Dimensions3 dimensions) {
    switch(shape) {
      case CUBE -> {
        return m_modelBuilder.createBox(
            dimensions.getWidth(), dimensions.getHeight(), dimensions.getDepth(),
            new Material(TextureAttribute.createDiffuse(texture)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
      }
      case SPHERE -> {
        return m_modelBuilder.createSphere(
            dimensions.getWidth(), dimensions.getHeight(), dimensions.getDepth(), 100, 100,
            new Material(TextureAttribute.createDiffuse(texture)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
      }
      default -> {
        HeliumIO.error("Model Builder", new IllegalArgumentException("Unknown shape type " + shape),
            HeliumIO.ErrorType.FATAL_CLOSE_IMMEDIATELY, true);
        return null;
      }
    }
  }

  public Model create(String path, Texture texture, ModelType type) {
    switch(type) {
      case GLTF -> {
        return m_GLTFLoader.load(Gdx.files.internal(path)).scene.model;
      }
      case OBJ -> {
        return m_objLoader.loadModel(Gdx.files.internal(path));
      }
      default -> {
        HeliumIO.error("Model Builder", new IllegalArgumentException("Unknown model type " + type),
            HeliumIO.ErrorType.FATAL_CLOSE_IMMEDIATELY, true);
        return null;
      }
    }
  }

  public enum Shape {
    SPHERE, CUBE
  }

  public enum ModelType {
    GLTF, OBJ
  }
}
