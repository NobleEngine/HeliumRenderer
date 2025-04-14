package org.noble.helium.handling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import org.noble.helium.Helium;
import org.noble.helium.actors.PlayerController;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;

import java.util.HashMap;
import java.util.Map;

public class ShaderHandler implements ShaderProvider {
  private static ShaderHandler m_instance;
  private final HashMap<String, ShaderProgram> m_shaders;
  private DefaultShader m_activeShader;
  private boolean m_isWireframe = false;

  private ShaderHandler() {
    m_shaders = new HashMap<>();
    ShaderProgram.pedantic = false;
    add("test", new ShaderProgram(Gdx.files.internal("assets/shaders/Test/test.vert"), Gdx.files.internal("assets/shaders/Test/test.frag")));
    setActiveShader("test");
  }

  public static ShaderHandler getInstance() {
    if (m_instance == null) {
      m_instance = new ShaderHandler();
    }
    return m_instance;
  }

  public void add(String name, ShaderProgram shader) {
    m_shaders.put(name, shader);
    if(!shader.isCompiled()) {
      HeliumTelemetry.getInstance().printErrorln("Could not compile shader " + name);
      HeliumTelemetry.getInstance().printErrorln(shader.getLog());
    }
  }

  public void setActiveShader(String name) {
//    m_activeShader = m_shaders.get(name);
  }

  public void toggleWireframe() {
    m_isWireframe = !m_isWireframe;
  }

  public void begin() {
    if(m_activeShader == null) {
      return;
    }
    m_activeShader.begin(PlayerController.getInstance().getCamera(), Helium.getInstance().getModelBatch().getRenderContext());
  }

  public void end() {
    m_activeShader.end();
  }

  public String getName(ShaderProgram shader) {
    for(Map.Entry<String, ShaderProgram> entry : m_shaders.entrySet()) {
      if(entry.getValue() == shader) {
        return entry.getKey();
      }
    }
    return null;
  }

  public DefaultShader getActiveShader() {
    return m_activeShader;
  }

  @Override
  public Shader getShader(Renderable renderable) {
    if(m_activeShader == null) {
      m_activeShader = new DefaultShader(renderable);
      m_activeShader.init();
    }
    if(m_isWireframe) {
      renderable.meshPart.primitiveType = GL20.GL_LINES;
    }
    return m_activeShader;
  }

  @Override
  public void dispose() {

  }
}